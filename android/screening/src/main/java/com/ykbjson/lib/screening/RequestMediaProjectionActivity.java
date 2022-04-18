package com.ykbjson.lib.screening;

import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ykbjson.lib.screening.listener.OnRequestMediaProjectionResultCallback;
import com.ykbjson.lib.screenrecorder.Notifications;
import com.ykbjson.lib.screenrecorder.ScreenRecordService;

/**
 * Description：获取录屏的MediaProjection的activity,显示一个像素
 * <BR/>
 * Creator：yankebin
 * <BR/>
 * CreatedAt：2019-08-06
 */
public class RequestMediaProjectionActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_MEDIA_PROJECTION = 1012;
    static OnRequestMediaProjectionResultCallback resultCallback;
    private MediaProjectionManager mMediaProjectionManager;
    private Notification notification;
    private static Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_media_projection);
        //window大小设置为1个像素，用户无感知不可见
        Window window = getWindow();
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.x = 0;
        params.y = 0;
        params.width = 1;
        params.height = 1;
        window.setAttributes(params);

        Intent intent = getIntent();
        notification = intent.getParcelableExtra("notification");
        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestMediaProjection();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_REQUEST_MEDIA_PROJECTION) {
            Intent intent = new Intent(mContext, ScreenRecordService.class);
            intent.putExtra("notification", notification);
            intent.putExtra("resultCode", resultCode);
            intent.putExtra("data", data);

            mContext.bindService(intent, new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    ScreenRecordService.MyBinder myBinder = (ScreenRecordService.MyBinder) service;
                    MediaProjection mediaProjection = myBinder.getMediaProjection();
                    if (mediaProjection == null) {
                        Log.e("@@", "media projection is null");
                        return;
                    }

                    resultCallback.onMediaProjectionResult(mediaProjection);
                    finish();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            }, Context.BIND_AUTO_CREATE);
         /*   MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            if (null == mediaProjection) {
                Toast.makeText(this, "你拒绝了录屏操作！", Toast.LENGTH_SHORT).show();
            } else if (null != resultCallback) {
                resultCallback.onMediaProjectionResult(mediaProjection);
            }*/

        }
    }

    @Override
    protected void onDestroy() {
        resultCallback = null;
        mContext = null;
        super.onDestroy();
    }

    private void requestMediaProjection() {
        Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
        startActivityForResult(captureIntent, CODE_REQUEST_MEDIA_PROJECTION);
    }

    static void start(Context context, Notification notification) {
        mContext = context;
        Intent intent = new Intent(context, RequestMediaProjectionActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("notification", notification);
        context.startActivity(intent);
    }
}
