package com.framework.core.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.framework.core.R;
import com.framework.core.common.WaitDialog;
import com.framework.core.tools.ToastTools;
import com.framework.core.utils.StatusbarColorUtils;
import com.framework.core.viewcontroller.VaryViewHelperController;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;


/**
 * Created by quxiang on 2016/12/28.
 */
public abstract class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate{

    SystemBarTintManager tintManager;
    public VaryViewHelperController mVaryViewHelperController = null;

    Toolbar toolbar;
    TextView toolbar_title;

    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        ActivityManager.getInstance().pushOneActivity(this);
        setTranslucentStatus(true);
        tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        changeThemeColor(Color.TRANSPARENT);
        if (getContentLayoutID()>0){
            setContentView(getContentLayoutID());
        }

        //如果淡色的actionbar颜色  miui 和 flyme 要设置状态栏颜色
        //Miui状态栏暗色
        setMiuiStatusTheme();
        //Flyme状态栏暗色
        StatusbarColorUtils.setStatusBarDarkIcon(this,true);  //参数 false 白色 true 黑色
    }

    public void setMiuiStatusTheme() {
        Window window = getWindow();
        Class clazz = window.getClass();
        try {
            int tranceFlag = 0;
            int darkModeFlag = 0;
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");

            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_TRANSPARENT");
            tranceFlag = field.getInt(layoutParams);

            field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);

            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
//            //只需要状态栏透明
//            extraFlagField.invoke(window, tranceFlag, tranceFlag);
            //状态栏透明且黑色字体
            extraFlagField.invoke(window, tranceFlag | darkModeFlag, tranceFlag | darkModeFlag);
//            //清除黑色字体
//            extraFlagField.invoke(window, 0, darkModeFlag);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackManager.getInstance().init(this) 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(false);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        if (toolbar!=null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);//不显示标题
            toolbar_title=(TextView)findViewById(R.id.toolbar_title);
            showBack(true);
        }
        initValue();
    }

    @Override
    public void setTitle(CharSequence title) {
        if (toolbar_title!=null){
            toolbar_title.setText(title);
        }
    }

    @Override
    public void setTitle(int titleId) {
        if (toolbar_title!=null){
            toolbar_title.setText(titleId);
        }
    }

    public void setTitleColor(int textColor) {
        if (toolbar_title!=null){
            toolbar_title.setTextColor(textColor);
        }
    }

    public void showBack(boolean isShowBack){
        if (isShowBack){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        }
    }

    public abstract int getContentLayoutID();

    public abstract void initValue();

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

    public void changeThemeColor(int color){
        if (tintManager!=null){
            tintManager.setStatusBarTintColor(color);
        }
    }

    //想要使用状态布局,就要初始化一个TagVIew （这个view一定要有个ParentVIew）
    public void initLoadingTargetView(View v) {
        mVaryViewHelperController = new VaryViewHelperController(v);
    }

    /**
     * toggle show loading
     *
     * @param toggle
     */
    protected void toggleShowLoading(boolean toggle, String msg) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }

        if (toggle) {
            mVaryViewHelperController.showLoading(msg);
        } else {
            mVaryViewHelperController.restore();
        }
    }


    /**
     * toggle show empty
     *
     * @param toggle
     */
    protected void toggleShowEmpty(boolean toggle, String msg,String btnText, View.OnClickListener onClickListener) {
        if (null == mVaryViewHelperController) {
            throw new IllegalArgumentException("You must return a right target view for loading");
        }
        if (toggle) {
            mVaryViewHelperController.showEmpty(msg,btnText, onClickListener);
        } else {
            mVaryViewHelperController.restore();
        }
    }

    public void doActivity(Class c) {
        startActivity(new Intent(this, c));
    }

    public void doActivity(Class c,boolean isFinish) {
        startActivity(new Intent(this, c));
        if (isFinish){
            finish();
        }
    }

    public void doActivity(Class c, Bundle bundle) {
        Intent intent = new Intent(this, c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void doActivityForResult(Class c,Bundle bundle,int request){
        Intent intent = new Intent(this, c);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, request);
    }

    public void showToast(String text){
        if (TextUtils.isEmpty(text)){
            return;
        }
        ToastTools.showToast(this,text);
    }

    WaitDialog dialog;

    public void showDialog(String msg){
        dialog=new WaitDialog(this);
        if (!TextUtils.isEmpty(msg)){
            dialog.setContent(msg);
        }else {
            dialog.setContent("正在请求数据");
        }
        dialog.show();
    }

    public void closeDialog(){
        if (dialog!=null){
            dialog.dismiss();
            dialog=null;
        }
    }


    public <T extends View>T getView(View v,int resid){
        return (T)v.findViewById(resid);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: //这个就是toolbar的返回按钮空间的id
                finish();
                break;
        }
        return true; //告诉系统我们自己处理了点击事件
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        ActivityManager.getInstance().popOneActivity(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }
}
