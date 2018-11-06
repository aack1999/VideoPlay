package com.aution.paidd.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.framework.core.utils.LogUtils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by quxiang on 2017/3/3.
 */

public class BitmapUtils {

    /**
     * 方法描述：压缩Resources类型的Bitmap
     *
     * @param resources Class for accessing an application's resources.
     * @param resId     图片资源的ID
     * @param reqWidth  Bitmap目标压缩宽度像素值
     * @param reqHeight Bitmap目标压缩高度像素值
     * @return 压缩后的Bitmap类型的图片
     */
    public static Bitmap decodeSampleBitmapFromResource(Resources resources, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        if (options.inSampleSize<=1){
            options.inSampleSize=2;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    /**
     * 方法描述：计算采样率inSampleSize的值
     *
     * @param options   BitmapFactory.Options实例
     * @param reqWidth  Bitmap目标压缩宽度像素值
     * @param reqHeight Bitmap目标压缩高度像素值
     * @return 采样率inSampleSize的值
     */
    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {

        //Raw height and width of image
        int height = options.outHeight;
        LogUtils.e("图片的搞="+height+"控件的高="+reqHeight);
        int width = options.outWidth;
        LogUtils.e("图片的宽"+width+"控件的宽="+reqWidth);

        int inSampleSize = 1;
        int width_inSampleSize = width / reqWidth;
        LogUtils.e("宽的缩小赔率="+width_inSampleSize);
        int height_inSampleSize = height / reqHeight;
        LogUtils.e("高的缩小赔率="+height_inSampleSize);
        if (width_inSampleSize > inSampleSize || height_inSampleSize > inSampleSize) {
            inSampleSize = width_inSampleSize > height_inSampleSize ? width_inSampleSize : height_inSampleSize;
        }
        LogUtils.e("压缩了="+inSampleSize);
        return inSampleSize;
    }

    /**
     * 创建平铺bitmap
     * @param width
     * @return
     */
    public static Bitmap createRepeater(int width, Bitmap mbitmap){
        Bitmap src = Bitmap.createScaledBitmap(mbitmap, 32, 16, true);
        int count = (width + src.getWidth() - 1) / src.getWidth();
        Bitmap bitmap = Bitmap.createBitmap(width, 16, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        for(int idx = 0; idx < count; ++ idx){
            canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
        }
        return bitmap;
    }

    public static String saveBitmapToFile(Bitmap bitmap,String path,int resid){
        File dirFile = new File(path);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }
        File myCaptureFile = new File(path + resid+".jpg");
        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
            return myCaptureFile.getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return "";
    }


    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "cache_qrcode");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
    }

    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }
}
