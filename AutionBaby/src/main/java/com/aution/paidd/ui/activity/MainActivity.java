package com.aution.paidd.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.framework.core.base.BaseActivity;
import com.framework.core.utils.LogUtils;
import com.aution.paidd.R;
import com.aution.paidd.bean.ServceMessageBean;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.broadcast.NetBroadcastReceiver;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CommonTools;
import com.aution.paidd.common.Constant;
import com.aution.paidd.common.HttpMethod;
import com.aution.paidd.model.MessageEvent;
import com.aution.paidd.request.InitLoginRequest;
import com.aution.paidd.response.LoginResponse;
import com.aution.paidd.service.ConnectService;
import com.aution.paidd.service.DownAPKService;
import com.aution.paidd.ui.adapter.FragmentAdapter;
import com.aution.paidd.ui.fragment.AllsFragment;
import com.aution.paidd.ui.fragment.DoneFragment;
import com.aution.paidd.ui.fragment.HomeFragment;
import com.aution.paidd.ui.fragment.MineFragment;
import com.aution.paidd.ui.widget.BottomNavigatorView;
import com.aution.paidd.utils.NetUtil;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import me.xiaopan.sketch.SketchImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

public class MainActivity extends BaseActivity implements NetBroadcastReceiver.NetEvevt {

    @BindView(R.id.botton_view)
    BottomNavigatorView botton_view;
    @BindView(R.id.message_icon)
    SketchImageView message_icon;
    @BindView(R.id.noNetView)
    View noNetView;

    FragmentNavigator mNavigator;
    List<Fragment> fragments;

    HomeFragment homeFragment;
    MineFragment mineFragment;
    AllsFragment allsFragment;

    public static NetBroadcastReceiver.NetEvevt evevt;

    /**
     * 网络类型
     */
    private int netMobile;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    public void initValue() {
        evevt = this;
        EventBus.getDefault().register(this);

        fragments = new ArrayList<>();

        fragments.add(homeFragment = new HomeFragment());
        fragments.add(new DoneFragment());
        fragments.add(allsFragment = new AllsFragment());
        fragments.add(mineFragment = new MineFragment());

        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(fragments), R.id.container);
        botton_view.setOnBottomNavigatorViewItemClickListener(new BottomNavigatorView.OnBottomNavigatorViewItemClickListener() {
            @Override
            public void onBottomNavigatorViewItemClick(int position, View view) {
                selectTab(position);
            }
        });
        selectTab(0);
        initNoNetView();
        if (inspectNet()) {
            //开启socket服务
            Intent intent = new Intent(this, ConnectService.class);
            startService(intent);
            noNetView.setVisibility(View.GONE);
            botton_view.setClickable(true);
        } else {
            noNetView.setVisibility(View.VISIBLE);
            botton_view.setClickable(false);
        }


        UserBean userBean = CacheData.getInstance().getUserData();
        if (userBean != null && !TextUtils.isEmpty(userBean.getUid())) {
            JPushInterface.setAlias(this, userBean.getUid(), new TagAliasCallback() {
                @Override
                public void gotResult(int i, String s, Set<String> set) {
                    LogUtils.e("极光别名id=" + s);
                }

            });
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.BroadcastAction);
        registerReceiver(myReceiver, filter);

//        CommonTools.upApp(this);
    }

    public void initNoNetView() {
        message_icon.displayResourceImage(R.drawable.ic_wwl);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {

        switch (event.getMsg()) {
            case 300:
                mineFragment.getUserInfo();
                break;
            case 145:
                selectTab(0);
                break;
            case 102:
                selectTab(0);
                initLogin();
                break;
        }
    }

    public void selectTab(int position) {
        if (position == 3) {
            mineFragment.getUserInfo();
        }

        botton_view.select(position);
        mNavigator.showFragment(position, false, true);
    }


    private void initLogin() {
        UserBean bean = CacheData.getInstance().getUserData();
        InitLoginRequest request = new InitLoginRequest();
        if (!TextUtils.isEmpty(bean.getUid())) {
            request.setUid(bean.getUid());
        }
        if (!TextUtils.isEmpty(bean.getOpenid())) {
            request.setOpenid(bean.getOpenid());
        }
        request.setPwd(bean.getPwd());
        request.setImei(CacheData.getInstance().getImei());
        Subscriber<LoginResponse> subscriber = new Subscriber<LoginResponse>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(LoginResponse response) {
                if (response != null) {
                    if (response.getCode() == 10000 && response.getObj() != null) {
                        CacheData.getInstance().cacheUserData(response.getObj());
                    }
                }
            }
        };
        HttpMethod.getInstance().getInitLogin(subscriber, request);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //关掉websocket
        unregisterReceiver(myReceiver);
        stopService(new Intent(this, ConnectService.class));
        stopService(new Intent(this, DownAPKService.class));

        EventBus.getDefault().unregister(this);
        MobclickAgent.onKillProcess(this);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void onNetChange(int netMobile) {
        this.netMobile = netMobile;
        if (isNetConnect()) {
            //有网络
            //开启socket服务
            Intent intent = new Intent(this, ConnectService.class);
            startService(intent);
            noNetView.setVisibility(View.GONE);
            botton_view.setClickable(true);
        } else {
            noNetView.setVisibility(View.VISIBLE);
            botton_view.setClickable(false);
        }
    }

    /**
     * 初始化时判断有没有网络
     */

    public boolean inspectNet() {
        this.netMobile = NetUtil.getNetWorkState(this);
        return isNetConnect();
    }

    /**
     * 判断有无网络 。
     *
     * @return true 有网, false 没有网络.
     */
    public boolean isNetConnect() {
        if (netMobile == 1) {
            return true;
        } else if (netMobile == 0) {
            return true;
        } else if (netMobile == -1) {
            return false;

        }
        return false;
    }


    private BroadcastReceiver myReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
//            ServceMessageBean bean = (ServceMessageBean) intent.getSerializableExtra("model");
//            if (homeFragment != null) {
//                homeFragment.upShopState(bean.copy());
//            }
//            if (allsFragment != null) {
//                allsFragment.upShopState(bean.copy());
//            }
        }

    };




}
