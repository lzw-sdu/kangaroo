package com.faceunity.app_ptag.ui.build_avatar.entity;

/**
 * Created on 2021/8/25 0025 17:27.
 * Author: Jason Lu
 * Email:tenglu@faceunity.com
 */
public class FaceInfo {
    private int faceCount;
    private float[] rotationData;
    private float[] faceRect;
    private float[] expressionData;

    public FaceInfo(int faceCount, float[] rotationData, float[] faceRect, float[] expressionData) {
        this.faceCount = faceCount;
        this.rotationData = rotationData;
        this.faceRect = faceRect;
        this.expressionData = expressionData;
    }

    public int getFaceCount() {
        return faceCount;
    }

    public float[] getRotationData() {
        return rotationData;
    }

    public float[] getFaceRect() {
        return faceRect;
    }

    public float[] getExpressionData() {
        return expressionData;
    }
}
