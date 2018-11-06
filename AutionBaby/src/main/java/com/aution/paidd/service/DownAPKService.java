package com.aution.paidd.service;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;


import com.aution.paidd.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownAPKService extends Service {
    private Context context;
    private static final String TAG = "ServiceDownAPK";
    public static final String ACTION = "com.fanli.service.ServiceDownAPK";
    private static final int DOWN_UPDATE = -1;
    private static final int DOWN_OVER = -2;
    private static boolean interceptFlag = false;
    private static int progress;
    public static final String URLFLAG = "url";
    // 自定义通知栏类
   private UpdateNotification myNotification;
    private static final String savePath = "/sdcard/CFOApp/";
    private static final String saveFileName = savePath + "ttjph.apk";
    private static final String notificationName = "天天竞拍行";
    private String urlAddress = null;
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            Looper.prepare();
            try {

                URL url = new URL(urlAddress);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);
                int times = 0;
                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    if (times >= 512 || progress == 100) {
                        times = 0;
                        mHandler.sendEmptyMessage(progress);
                        if (numread <= 0) {
                            //下载完成通知安装
                            mHandler.sendEmptyMessage(DOWN_OVER);
                            break;
                        }
                    }
                    times++;
                    try {
                        fos.write(buf, 0, numread);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }

                } while (!interceptFlag);//点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Looper.loop();

        }
    };

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    break;
                case DOWN_OVER:
                   myNotification.removeNotification();
                    installApk();
                    break;
                default:
                    myNotification.changeProgressStatus(msg.what);
                    break;
            }
        }

        ;
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        urlAddress = intent.getStringExtra(URLFLAG);
        //Log.e("", "下载地址:"+urlAddress);

        if (urlAddress != null) {

            new Thread(mdownApkRunnable).start();
            Toast.makeText(getApplicationContext(), "开始更新下载", Toast.LENGTH_LONG).show();
            PendingIntent pendingintent = PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);
            myNotification = new UpdateNotification(this, pendingintent, 1);
            myNotification.showCustomizeNotification(R.drawable.icon,
                    notificationName, R.layout.notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.addCategory("android.intent.category.DEFAULT");
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Toast.makeText(getApplicationContext(), "下载完成", Toast.LENGTH_SHORT).show();
        startActivity(i);
        DownAPKService.this.onDestroy();
    }
}
