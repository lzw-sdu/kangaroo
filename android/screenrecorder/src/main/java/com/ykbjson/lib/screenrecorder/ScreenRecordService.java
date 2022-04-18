package com.ykbjson.lib.screenrecorder;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

public class ScreenRecordService extends Service {

    private MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mediaProjection;
    MyBinder myBinder;
    private Notification mNotification;
    private final int id = 1;
    private Intent data;
    private int resultCode;

    @Override
    public IBinder onBind(Intent intent) {
        mNotification = intent.getParcelableExtra("notification");
        data = intent.getParcelableExtra("data");
        resultCode = intent.getIntExtra("resultCode", 0);
        startForeground(id, mNotification);
        mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
        if (mediaProjection == null) {
            Toast.makeText(this, "你拒绝了录屏操作！", Toast.LENGTH_SHORT).show();
        }
        return myBinder;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public boolean onUnbind(Intent intent) {
        stopForeground(STOP_FOREGROUND_REMOVE);
        Log.d("MyService", "onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mNotification = null;
        super.onDestroy();
        Log.d("MyService", "onDestroy()");
    }

    @Override
    public void onCreate() {
        Log.d("ScreenRecordService", "服务被创建了");
        mMediaProjectionManager = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
        myBinder = new MyBinder();
        super.onCreate();
    }

    public class MyBinder extends Binder {
        public MediaProjection getMediaProjection() {
            if (mediaProjection == null) {
                mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            }
            return mediaProjection;
        }
    }
}
