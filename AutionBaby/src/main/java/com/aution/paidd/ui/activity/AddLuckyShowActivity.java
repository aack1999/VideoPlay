package com.aution.paidd.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.framework.core.base.BaseActivity;
import com.framework.core.utils.LogUtils;
import com.framework.core.widget.FlatButton;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.lzy.okgo.request.PostRequest;
import com.aution.paidd.R;
import com.aution.paidd.bean.SelectImageBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CropPhotoTools;
import com.aution.paidd.common.Url;
import com.aution.paidd.response.BaseResponse;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import me.xiaopan.sketch.SketchImageView;
import okhttp3.Call;
import okhttp3.Response;



/**
 * 添加晒单
 * Created by yangjie on 2016/10/29.
 */
public class AddLuckyShowActivity extends BaseActivity {

    @BindView(R.id.ed_content)
    EditText ed_content;
    @BindView(R.id.btn_submit)
    FlatButton btn_submit;
    @BindView(R.id.ed_title)
    EditText ed_title;
    @BindView(R.id.girdview)
    GridView girdview;

    List<SelectImageBean> mDataList;

    int i;

    CropPhotoTools cropPhotoTools;
//    HashMap<Integer, File> fileList;

    MyAdapter myAdapter;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_add_lucky_show;
    }

    @Override
    public void initValue() {
        setTitle("晒单");
        cropPhotoTools = new CropPhotoTools(this);

        mDataList=new ArrayList<>();
        girdview.setAdapter(myAdapter=new MyAdapter());
        mDataList.add(new SelectImageBean());
        myAdapter.notifyDataSetChanged();
    }


    private void initPop() {
        cropPhotoTools.setIsCrop(false);
        cropPhotoTools.startCrop(new CropPhotoTools.OnCropPhotoLisenter() {
            @Override
            public void onOk(String path) {
                mDataList.add(mDataList.size()-1,new SelectImageBean(path));
                if (mDataList.size()>3){
                    mDataList.remove(mDataList.size()-1);
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onUpOk(boolean isOk) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cropPhotoTools.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        cropPhotoTools.onActivityResult(requestCode, resultCode, data);
    }


    public void sendImageNetData() {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("正在提交数据...");
        List<File> flist = new ArrayList<>();

        for (int j = 0; j < mDataList.size() ; j++) {
            if (!TextUtils.isEmpty(mDataList.get(j).getPath())){
                flist.add(new File(mDataList.get(j).getPath()));
            }
        }

        PostRequest pr = OkGo.post(Url.getAddLuckyShow()).tag(this);
        for (int j = 0; j < flist.size(); j++) {
            pr.params("imgs" + j, flist.get(j));
        }

        pr.params("uid", CacheData.getInstance().getUserData().getUid() + "")
                .params("aid", getIntent().getStringExtra("aid"))
                .params("orderno",getIntent().getStringExtra("orderno"))
                .params("title", ed_title.getText().toString())
                .params("content", ed_content.getText().toString())
                .execute(new StringCallback() {

                    @Override
                    public void onBefore(BaseRequest request) {
                        super.onBefore(request);
                        dialog.show();
                    }

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //上传成功
                        dialog.dismiss();
                        processImageData(s);
                    }


                    @Override
                    public void upProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        //这里回调上传进度(该回调在主线程,可以直接更新ui)
                        LogUtils.e(totalSize + " " + progress);
                        dialog.setProgress((int) (progress * 100));
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        showToast("上次失败");
                    }
                });
    }

    protected void processImageData(String result) {
        Gson gson = new Gson();
        BaseResponse registerUserBean = gson.fromJson(result, BaseResponse.class);
        if (registerUserBean!=null) {
            showToast(registerUserBean.getMsg());
            if (registerUserBean.getCode()==10000){
                setResult(200);
                finish();
            }
        }
    }


    @OnClick({R.id.btn_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                if (checkIsEmpty()) {
                    sendImageNetData();
                }
                break;

        }
    }

    private boolean checkIsEmpty() {
        if (TextUtils.isEmpty(ed_title.getText().toString())) {
            showToast("标题不能为空");
            return false;
        } else if (TextUtils.isEmpty(ed_content.getText().toString())) {
            showToast("内容不能为空");
            return false;
        }
        return true;
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mDataList.size();
        }

        @Override
        public Object getItem(int i) {
            return mDataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            MyViewHodler hodler;
            if (view==null){
                view=getLayoutInflater().inflate(R.layout.select_img_item,viewGroup,false);
                hodler=new MyViewHodler(view);
                view.setTag(hodler);
            }else {
                hodler=(MyViewHodler) view.getTag();
            }

            hodler.onBindData(i);
            return view;
        }

        class MyViewHodler {

            SketchImageView ll_pic_one;

            ImageView btn_clear_1;

            public MyViewHodler(View view) {
                ll_pic_one=(SketchImageView)view.findViewById(R.id.ll_pic_one);
                btn_clear_1=(ImageView)view.findViewById(R.id.btn_clear_1);
            }

            public void onBindData(final int position){

                if (TextUtils.isEmpty(mDataList.get(position).getPath())){
                    ll_pic_one.displayResourceImage(R.drawable.fabushaidan);
                    btn_clear_1.setVisibility(View.GONE);
                    ll_pic_one.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            i=position;
                            initPop();
                        }
                    });
                }else {
                    ll_pic_one.displayImage(mDataList.get(position).getPath());
                    btn_clear_1.setVisibility(View.VISIBLE);
                    ll_pic_one.setOnClickListener(null);
                }


                btn_clear_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDataList.remove(position);
                        if (!TextUtils.isEmpty(mDataList.get(mDataList.size()-1).getPath())){
                            mDataList.add(new SelectImageBean());
                        }
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }
}
