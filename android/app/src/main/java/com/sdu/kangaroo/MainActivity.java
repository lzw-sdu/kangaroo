package com.sdu.kangaroo;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.faceunity.app_ptag.FuDevInitializeWrapper;
import com.sdu.kangaroo.handler.DLNAHandler;
import com.sdu.kangaroo.handler.DanceHandler;
import com.sdu.kangaroo.utils.start_avtivity_result.StartActivityManger;
import com.sdu.kangaroo.simplepermission.PermissionsManager;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    private static final String DLNA_CHANNEL = "com.sdu.kangaroo/dlna";
    private static final String DANCE_CHANNEL = "com.sdu.kangaroo/dance";
    private static final String DEVICE_CHANNEL = "com.sdu.kangaroo/device";
    public static final int CODE_REQUEST_MEDIA = 1011;

    public static MethodChannel deviceChannel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FuDevInitializeWrapper.INSTANCE.initSDK(this);
    }

    /**
     * Hook for subclasses to easily configure a {@code FlutterEngine}.
     *
     * <p>This method is called after .
     *
     * <p>All plugins listed in the app's pubspec are registered in the base implementation of this
     * method unless the FlutterEngine for this activity was externally created. To avoid the
     * automatic plugin registration for implicitly created FlutterEngines, override this method
     * without invoking super(). To keep automatic plugin registration and further configure the
     * FlutterEngine, override this method, invoke super(), and then configure the FlutterEngine as
     * desired.
     *
     * @param flutterEngine
     */
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
        GeneratedPluginRegistrant.registerWith(flutterEngine);
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), DLNA_CHANNEL).setMethodCallHandler(DLNAHandler.Companion.getInstance(this));
        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), DANCE_CHANNEL).setMethodCallHandler(DanceHandler.Companion.getInstance(this));
        deviceChannel = new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), "com.sdu.kangaroo/device");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        StartActivityManger.Companion.getInstance().notifyActivityResult(requestCode, resultCode, data, this);
    }
}


