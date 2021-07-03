package com.example.yf.testgitapplication.print_demo;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;

import com.example.yf.testgitapplication.R;
import com.example.yf.testgitapplication.print_demo.ui.PrintActivity;
/********************************************************************
  @version: 1.0.0
  @description: 前台服务--常驻通知栏
  @author: EDZ
  @time: 2019/8/8 9:49
  @变更历史:
********************************************************************/
public class FrontPrintService extends Service {
    public FrontPrintService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(Build.VERSION.SDK_INT>=26){
            setForeground();
        }else{
//         在API11之后构建Notification的方式
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
            Intent nfIntent = new Intent(this, PrintActivity.class);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle("我的项目") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.logo) // 设置状态栏内的小图标
                    .setContentText("请保持程序在后台运行") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
            Notification notification = builder.build(); // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(0x111, notification);// 开始前台服务
        }
    }
    @TargetApi(26)
    private void setForeground() {
        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel ("小二生活","小二商家版",NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Intent nfIntent = new Intent(this, PrintActivity.class);
        Notification notification=new Notification.Builder (this,"小二生活")
                .setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle ("我的项目")
                .setSmallIcon (R.mipmap.logo)
                .setContentText ("请保持程序在后台运行")
                .setWhen(System.currentTimeMillis()) // 设置该通知发生的时间
                .build ();
        startForeground (0x111,notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      /*  Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, PrintActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo)) // 设置下拉列表中的图标(大图标)
                .setContentTitle("我的项目") // 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.logo) // 设置状态栏内的小图标
                .setContentText("请保持程序在后台运行") // 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
        Notification notification = builder.build(); // 获取构建好的Notification
//        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(0x111, notification);// 开始前台服务*/
        return super.onStartCommand(intent, flags, startId);
    }
}
