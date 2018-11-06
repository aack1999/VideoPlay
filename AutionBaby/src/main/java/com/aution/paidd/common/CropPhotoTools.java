package com.aution.paidd.common;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.framework.core.utils.DialogUtils;

import com.google.gson.Gson;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.framework.core.utils.LogUtils;
import com.aution.paidd.response.LoginResponse;
import com.aution.paidd.utils.PhotoUtils;
import com.soundcloud.android.crop.Crop;


import java.io.File;
import java.lang.ref.WeakReference;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2015/12/18 0018.
 */
public class CropPhotoTools {

    WeakReference<Activity> activity;

    String picPath="";

    String upPath="";

    boolean isCrop = true;//是否裁剪,默认为是
    Uri imageUri;//原图保存地址

    OnCropPhotoLisenter onCropPhotoLisenter;

    String aid;

    String title;

    String content;

    public CropPhotoTools(Activity activity) {
        this.activity = new WeakReference<Activity>(activity);
    }

    public void setIsCrop(boolean isCrop){
        this.isCrop=isCrop;
    }

    public void startCrop(OnCropPhotoLisenter onCropPhotoLisenter1) {
        if (activity.get()!=null){
            this.onCropPhotoLisenter = onCropPhotoLisenter1;
            ActionSheet.createBuilder(activity.get(), ((FragmentActivity) activity.get()).getSupportFragmentManager())
                    .setCancelButtonTitle("取消")
                    .setOtherButtonTitles("拍照", "图库")
                    .setCancelableOnTouchOutside(true).setListener(new ActionSheet.ActionSheetListener() {
                @Override
                public void onDismiss(ActionSheet actionSheet, boolean b) {

                }

                @Override
                public void onOtherButtonClick(ActionSheet actionSheet, int i) {
                    if (i == 0) {
                        takePhoto();
                    } else {
                        pickPhoto();
                    }
                }
            }).show();
        }
    }

    void takePhoto(){
        requestTakePhotoPermission();
    }

    void pickPhoto(){
        requestpickPhotoPermission();
    }


    private void beginCrop(Uri source) {
        File file = new File(activity.get().getCacheDir(), System.currentTimeMillis() + ".png");
        picPath = file.getAbsolutePath();
        Uri destination = Uri.fromFile(file);
        Crop.of(source, destination).asSquare().start(activity.get());
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == activity.get().RESULT_OK) {
            onCropPhotoLisenter.onOk(picPath);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(activity.get(), Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (requestCode == Constant.CAMERA_REQUEST_CODE) {
            if (isCrop) {
                beginCrop(mOutPutFileUri);
            } else {
                handleCrop(resultCode, result);
            }
            return;
        }
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            if (isCrop) {
                beginCrop(result.getData());
            }else {
                picPath = PhotoUtils.getImageAbsolutePath(activity.get(), result.getData());
                handleCrop(resultCode,result);
            }
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }

    }


    public String submitFile() {
        sendImageNetData(picPath);
        return upPath;
    }


    private void sendImageNetData(String path) {
        File file = new File(path);
        final UpFileDialog dialog=new UpFileDialog(activity.get());
        OkGo.post(Url.getUpLogoUrl())
                .tag(this)//
                .isMultipart(true)       // 强制使用 multipart/form-data 表单上传（只是演示，不需要的话不要设置。默认就是false）
                .params("uid", CacheData.getInstance().getUserData().getUid())        // 这里可以上传参数
                .params("imgfile", file)   // 可以添加文件上传
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
                        LogUtils.e(totalSize+" "+progress);
                        dialog.setProgress(progress);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        onCropPhotoLisenter.onUpOk(false);
                    }
                });
    }

    protected void processImageData(String result) {
        Gson gson = new Gson();
        LoginResponse registerUserBean = gson.fromJson(result, LoginResponse.class);

        if (registerUserBean!=null){
            if (registerUserBean.getCode()==10000){
                CacheData.getInstance().cacheLogoFile(getPicPath());
                CacheData.getInstance().cacheUserData(registerUserBean.getObj());
                onCropPhotoLisenter.onUpOk(true);
                return;
            }
        }
        onCropPhotoLisenter.onUpOk(false);
    }



    public String getUpPath() {
        return upPath;
    }


    public void setPicPath(String str){
        picPath=str;
    }

    public String getPicPath() {
        return picPath;
    }


    public interface OnCropPhotoLisenter {
        void onOk(String path);

        void onUpOk(boolean isOk);
    }



    private Uri mOutPutFileUri;
    private void saveFullImage(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        //文件夹tomato
        String path = Environment.getExternalStorageDirectory().toString()+"/tomato/Camaro/";
        File path1 = new File(path);
        if(!path1.exists()){
            path1.mkdirs();
        }
        File file = new File(path1,System.currentTimeMillis()+".jpg");
        mOutPutFileUri = Uri.fromFile(file);
        picPath=file.getPath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        activity.get().startActivityForResult(intent, Constant.CAMERA_REQUEST_CODE);
    }

    private void requestTakePhotoPermission() {
        if (ActivityCompat.checkSelfPermission(activity.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity.get(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
        } else {
            //可以做事情了
            saveFullImage();
        }
    }

    private void requestpickPhotoPermission() {
        if (ActivityCompat.checkSelfPermission(activity.get(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions(activity.get(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);
        } else {
            //可以做事情了
            Crop.pickImage(activity.get());
        }
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过了
                saveFullImage();
            } else {
                checkPermission(100);
            }
        }else if (requestCode==200){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //权限通过了
                Crop.pickImage(activity.get());
            } else {
                checkPermission(200);
            }
        }
    }

    public void checkPermission(final int code){
        DialogUtils.showDialog(activity.get(), "上传图片需要访问相册或拍照权限，请允许!", "申请", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (code==100){
                    requestTakePhotoPermission();
                }else if (code==200){
                    requestpickPhotoPermission();
                }
            }
        });
    }

}
