package com.sdu.kangaroo.nginxserver.nginx;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.jrummyapps.android.shell.CommandResult;
import com.jrummyapps.android.shell.Shell;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RTSPClientHelper {
    private static final String TAG = "RTSPHelper";
    private static final String EXE_FILE_RELATIVE_PATH = "/rtsp-simple-server";
    private static final String CONF_FILE_RELATIVE_PATH = "/rtsp-simple-server.yml";
    private static String mRTSPDir;

    public static void installAndStartRTSPServer(@NonNull Context context) {
        mRTSPDir = getAppDataDir(context) + "/rtsp";
        copyFileOrDirFromAsset(context, "rtsp");
        copyFileOrDirFromAsset(context, "alphaVideoGift");
        CommandResult result = Shell.SH.run("chmod -R 777 " + mRTSPDir);
        CommandResult res = Shell.SH.run( mRTSPDir + EXE_FILE_RELATIVE_PATH + " " + mRTSPDir + CONF_FILE_RELATIVE_PATH + " &");
        Log.d(TAG, result.exitCode + "\n" + result.stdout + "\n" + result.stderr);
        Log.d(TAG, res.exitCode + "\n" + res.stdout + "\n" + res.stderr);
    }

    private static String getAppDataDir(@NonNull Context context) {
        return context.getApplicationInfo().dataDir;
    }

    private static void copyFileOrDirFromAsset(@NonNull Context context, String path) {
        AssetManager assetManager = context.getAssets();
        String[] assets;
        try {
            assets = assetManager.list(path);
            if (null == assets || assets.length == 0) {
                copyFile(context, path);
            } else {
                String fullPath = getAppDataDir(context) + "/" + path;
                File dir = new File(fullPath);
                if (!dir.exists())
                    dir.mkdir();
                for (String asset : assets) {
                    copyFileOrDirFromAsset(context, path + "/" + asset);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyFile(@NonNull Context context, String filename) {
        Log.d(TAG, "copy file path : " + filename);
        AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            String newFileName = getAppDataDir(context) + "/" + filename;
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                }
                if (null != in) {
                    in.close();
                }
            } catch (IOException e) {
                //ignored
            }
        }
    }


    public static String getRTSPPort() {
        return ":8554/";
    }

}

