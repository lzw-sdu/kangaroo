package com.chillingvan.lib.camera;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;

/**
 * 
 */

public interface CameraInterface {
    void setPreview(SurfaceTexture surfaceTexture);

    void openCamera();

    void switchCamera();

    void switchCamera(int previewWidth, int previewHeight);

    boolean isOpened();

    void startPreview();

    void stopPreview();

    Camera getCamera();

    void release();
}
