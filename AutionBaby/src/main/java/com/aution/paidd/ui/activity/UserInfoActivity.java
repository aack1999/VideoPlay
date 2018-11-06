package com.aution.paidd.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import com.framework.core.base.BaseActivity;
import com.framework.core.widget.LogoRowCell;
import com.framework.core.widget.RowCell;
import com.framework.core.widget.RowCellGroupView;
import com.aution.paidd.R;
import com.aution.paidd.bean.UserBean;
import com.aution.paidd.common.CacheData;
import com.aution.paidd.common.CropPhotoTools;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rowcellgroupview)
    RowCellGroupView rowcellgroupview;

    CropPhotoTools cropPhotoTools;

    @Override
    public int getContentLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    public void initValue() {
        setTitle("我的信息");
        cropPhotoTools=new CropPhotoTools(this);
        upUserInfo();
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case 0:
                cropPhotoTools.startCrop(new CropPhotoTools.OnCropPhotoLisenter() {
                    @Override
                    public void onOk(String path) {
                        cropPhotoTools.submitFile();
                    }

                    @Override
                    public void onUpOk(boolean isOk) {
                        if (isOk) {
                            rowcellgroupview.getLogoImg().displayImage(cropPhotoTools.getPicPath());
                            setResult(200);
                        } else {
                            showToast("上传头像失败,请重试");
                        }
                    }
                });
                break;
            case 1: {
                Intent intent = new Intent();
                intent.setClass(this, ChangeUserInfoActivity.class);
                intent.putExtra("type", ChangeUserInfoActivity.TYPE_NAME);
                startActivityForResult(intent, 400);
            }
            break;
            case 3:
                Bundle bundle=new Bundle();
                bundle.putInt("type",2);
                doActivity(ChangePassWordActivity.class,bundle);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (cropPhotoTools != null) {
            cropPhotoTools.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (cropPhotoTools != null)
            cropPhotoTools.onActivityResult(requestCode, resultCode, data);

        upUserInfo();
    }


    public void upUserInfo() {
        UserBean userBean = CacheData.getInstance().getUserData();
        List<RowCell> arr = new ArrayList<>();
        arr.add(null);
        arr.add(new LogoRowCell().setImgUrl(userBean.getImg()).setLabel("头像").setViewid(0));
        arr.add(null);

        arr.add(new RowCell().setLabel("昵称").setDesc(userBean.getNickname()).setViewid(1));
        arr.add(new RowCell().setLabel("ID号").setDesc(userBean.getUid() + "").setAction(false));
        if (TextUtils.isEmpty(userBean.getOpenid())){
            arr.add(new RowCell().setLabel("修改密码").setViewid(3));
        }
        arr.add(null);
        rowcellgroupview.notifyDataChanged(arr);
        rowcellgroupview.setOnClickListener(this);
    }

    @Override
    public void finish() {
        setResult(200);
        super.finish();
    }


}
