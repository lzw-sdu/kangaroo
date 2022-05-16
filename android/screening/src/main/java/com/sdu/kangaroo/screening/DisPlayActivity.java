package com.sdu.kangaroo.screening;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.pedro.rtsp.utils.ConnectCheckerRtsp;
import com.sdu.kangaroo.nginxserver.nginx.RTSPClientHelper;
import com.sdu.kangaroo.screenrecorder.IRecorderCallback;

public class DisPlayActivity extends AppCompatActivity implements ConnectCheckerRtsp {

    private final int REQUEST_CODE_STREAM = 179; //random num
    private final int REQUEST_CODE_RECORD = 180; //random num
    static IRecorderCallback callback;

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


        startService();
//        notification = intent.getParcelableExtra("notification");
//        mMediaProjectionManager = (MediaProjectionManager) getSystemService(MEDIA_PROJECTION_SERVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null && (requestCode == REQUEST_CODE_STREAM
                || requestCode == REQUEST_CODE_RECORD && resultCode == Activity.RESULT_OK)) {
            DisplayService displayService = DisplayService.Companion.getINSTANCE();
            if (displayService != null) {
                String endpoint = "rtsp://" + DLNAManager.getLocalIpStr(this) +
                        RTSPClientHelper.getRTSPPort() + "mystream";
                displayService.prepareStreamRtp(endpoint, resultCode, data);
                displayService.startStreamRtp(endpoint);
            }
        } else {
            Toast.makeText(this, "No permissions available", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        stopService();
     /*   DisplayService displayService = DisplayService.Companion.getINSTANCE();
        if (displayService != null && !displayService.isStreaming() && !displayService.isRecording()) {
            //stop service only if no streaming or recording
            stopService(new Intent(this, DisplayService.class));
        }*/
    }

    static void start(Context context) {
        Intent intent = new Intent(context, DisPlayActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    void startService() {
        DisplayService displayService = DisplayService.Companion.getINSTANCE();
        if (displayService != null) {
            startActivityForResult(displayService.sendIntent(), REQUEST_CODE_STREAM);
        }
    }

    void stopService() {
        DisplayService displayService = DisplayService.Companion.getINSTANCE();
        displayService.stopStream();
    }

    @Override
    public void onAuthErrorRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisPlayActivity.this, "Auth error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAuthSuccessRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisPlayActivity.this, "Auth success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionFailedRtsp(@NonNull String reason) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisPlayActivity.this, "Connection failed. " + reason, Toast.LENGTH_SHORT)
                        .show();
                DisplayService displayService = DisplayService.Companion.getINSTANCE();
                if (displayService != null) {
                    displayService.stopStream();
                }
            }
        });
    }

    @Override
    public void onConnectionStartedRtsp(@NonNull String s) {

    }

    @Override
    public void onConnectionSuccessRtsp() {
        callback.onStartRecord();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisPlayActivity.this, "Connection success", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onDisconnectRtsp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DisPlayActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNewBitrateRtsp(long l) {

    }
}
