// Tencent is pleased to support the open source community by making ncnn available.
//
// Copyright (C) 2021 THL A29 Limited, a Tencent company. All rights reserved.
//
// Licensed under the BSD 3-Clause License (the "License"); you may not use this file except
// in compliance with the License. You may obtain a copy of the License at
//
// https://opensource.org/licenses/BSD-3-Clause
//
// Unless required by applicable law or agreed to in writing, software distributed
// under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
// CONDITIONS OF ANY KIND, either express or implied. See the License for the
// specific language governing permissions and limitations under the License.

#include <android/asset_manager_jni.h>
#include <android/native_window_jni.h>
#include <android/native_window.h>

#include <android/log.h>

#include <jni.h>

#include <string>
#include <vector>

#include <platform.h>
#include <benchmark.h>

#include "nanodet.h"

#include "ndkcamera.h"

#include <opencv2/core/core.hpp>
#include <opencv2/imgproc/imgproc.hpp>

#if __ARM_NEON
#include <arm_neon.h>
#endif // __ARM_NEON

static int draw_unsupported(cv::Mat &rgb) {
    const char text[] = "unsupported";

    int baseLine = 0;
    cv::Size label_size = cv::getTextSize(text, cv::FONT_HERSHEY_SIMPLEX, 1.0, 1, &baseLine);

    int y = (rgb.rows - label_size.height) / 2;
    int x = (rgb.cols - label_size.width) / 2;

    cv::rectangle(rgb, cv::Rect(cv::Point(x, y),
                                cv::Size(label_size.width, label_size.height + baseLine)),
                  cv::Scalar(255, 255, 255), -1);

    cv::putText(rgb, text, cv::Point(x, y + label_size.height),
                cv::FONT_HERSHEY_SIMPLEX, 1.0, cv::Scalar(0, 0, 0));

    return 0;
}

static int draw_fps(cv::Mat &rgb) {
    // resolve moving average
    float avg_fps = 0.f;
    {
        static double t0 = 0.f;
        static float fps_history[10] = {0.f};

        double t1 = ncnn::get_current_time();
        if (t0 == 0.f) {
            t0 = t1;
            return 0;
        }

        float fps = 1000.f / (t1 - t0);
        t0 = t1;

        for (int i = 9; i >= 1; i--) {
            fps_history[i] = fps_history[i - 1];
        }
        fps_history[0] = fps;

        if (fps_history[9] == 0.f) {
            return 0;
        }

        for (int i = 0; i < 10; i++) {
            avg_fps += fps_history[i];
        }
        avg_fps /= 10.f;
    }

    char text[32];
    sprintf(text, "FPS=%.2f", avg_fps);

    int baseLine = 0;
    cv::Size label_size = cv::getTextSize(text, cv::FONT_HERSHEY_SIMPLEX, 0.5, 1, &baseLine);

    int y = 0;
    int x = rgb.cols - label_size.width;

    cv::rectangle(rgb, cv::Rect(cv::Point(x, y),
                                cv::Size(label_size.width, label_size.height + baseLine)),
                  cv::Scalar(255, 255, 255), -1);

    cv::putText(rgb, text, cv::Point(x, y + label_size.height),
                cv::FONT_HERSHEY_SIMPLEX, 0.5, cv::Scalar(0, 0, 0));

    return 0;
}

static NanoDet *g_nanodet = 0;
static ncnn::Mutex lock;

class MyNdkCamera : public NdkCameraWindow {
public:
    virtual void on_image_render(cv::Mat &rgb) const;
};

void MyNdkCamera::on_image_render(cv::Mat &rgb) const {
    // 每一次采样
    {
        ncnn::MutexLockGuard g(lock);

        if (g_nanodet) {
            g_nanodet->detect(rgb);

            g_nanodet->draw(rgb);
        } else {
            draw_unsupported(rgb);
        }
    }

    draw_fps(rgb);
}

static MyNdkCamera *g_camera = 0;

extern "C" {

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnLoad");

    g_camera = new MyNdkCamera;

    return JNI_VERSION_1_4;
}

JNIEXPORT void JNI_OnUnload(JavaVM *vm, void *reserved) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "JNI_OnUnload");

    {
        ncnn::MutexLockGuard g(lock);

        delete g_nanodet;
        g_nanodet = 0;
    }

    delete g_camera;
    g_camera = 0;
}

// public native boolean loadModel(AssetManager mgr, int modelid, int cpugpu);
JNIEXPORT jboolean JNICALL
Java_com_example_camera_1kotlin_NcnnBodypose_loadModel(JNIEnv *env, jobject thiz,
                                                     jobject assetManager, jint modelid,
                                                     jint cpugpu) {
    //modelid目前只支持1、2
    if (modelid < 0 || modelid > 6 || cpugpu < 0 || cpugpu > 1) {
        return JNI_FALSE;
    }

    AAssetManager *mgr = AAssetManager_fromJava(env, assetManager);

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "loadModel %p", mgr);

    const char *modeltypes[] =
            {
                    "lightning",
                    "thunder",
            };

    const int target_sizes[] =
            {
                    192,
                    256,
            };

    const float mean_vals[][3] =
            {
                    {127.5f, 127.5f, 127.5f},
                    {127.5f, 127.5f, 127.5f},
            };

    const float norm_vals[][3] =
            {
                    {1 / 127.5f, 1 / 127.5f, 1 / 127.5f},
                    {1 / 127.5f, 1 / 127.5f, 1 / 127.5f},
            };

    const char *modeltype = modeltypes[(int) modelid];
    int target_size = target_sizes[(int) modelid];
    bool use_gpu = (int) cpugpu == 1;

    // reload
    {
        ncnn::MutexLockGuard g(lock);

        if (use_gpu && ncnn::get_gpu_count() == 0) {
            // no gpu
            delete g_nanodet;
            g_nanodet = 0;
        } else {
            if (!g_nanodet)
                g_nanodet = new NanoDet;
            g_nanodet->load(mgr, modeltype, target_size, mean_vals[(int) modelid],
                            norm_vals[(int) modelid], use_gpu);
        }
    }

    return JNI_TRUE;
}


cv::Mat on_image(const unsigned char *nv21, int nv21_width, int nv21_height) {
    int accelerometer_orientation = 0;
    int camera_orientation = 0;
    int camera_facing = 1;
    // roi crop and rotate nv21
    int nv21_roi_x = 0;
    int nv21_roi_y = 0;
    int nv21_roi_w = 0;
    int nv21_roi_h = 0;
    int roi_x = 0;
    int roi_y = 0;
    int roi_w = 0;
    int roi_h = 0;
    int rotate_type = 0;
    int render_w = 0;
    int render_h = 0;
    int render_rotate_type = 0;
    {
        int win_w = 640;
        int win_h = 480;

        if (accelerometer_orientation == 90 || accelerometer_orientation == 270) {
            std::swap(win_w, win_h);
        }

        const int final_orientation = (camera_orientation + accelerometer_orientation) % 360;

        if (final_orientation == 0 || final_orientation == 180) {
            if (win_w * nv21_height > win_h * nv21_width) {
                roi_w = nv21_width;
                roi_h = (nv21_width * win_h / win_w) / 2 * 2;
                roi_x = 0;
                roi_y = ((nv21_height - roi_h) / 2) / 2 * 2;
            } else {
                roi_h = nv21_height;
                roi_w = (nv21_height * win_w / win_h) / 2 * 2;
                roi_x = ((nv21_width - roi_w) / 2) / 2 * 2;
                roi_y = 0;
            }

            nv21_roi_x = roi_x;
            nv21_roi_y = roi_y;
            nv21_roi_w = roi_w;
            nv21_roi_h = roi_h;
        }
        if (final_orientation == 90 || final_orientation == 270) {
            if (win_w * nv21_width > win_h * nv21_height) {
                roi_w = nv21_height;
                roi_h = (nv21_height * win_h / win_w) / 2 * 2;
                roi_x = 0;
                roi_y = ((nv21_width - roi_h) / 2) / 2 * 2;
            } else {
                roi_h = nv21_width;
                roi_w = (nv21_width * win_w / win_h) / 2 * 2;
                roi_x = ((nv21_height - roi_w) / 2) / 2 * 2;
                roi_y = 0;
            }

            nv21_roi_x = roi_y;
            nv21_roi_y = roi_x;
            nv21_roi_w = roi_h;
            nv21_roi_h = roi_w;
        }

        if (camera_facing == 0) {
            if (camera_orientation == 0 && accelerometer_orientation == 0) {
                rotate_type = 2;
            }
            if (camera_orientation == 0 && accelerometer_orientation == 90) {
                rotate_type = 7;
            }
            if (camera_orientation == 0 && accelerometer_orientation == 180) {
                rotate_type = 4;
            }
            if (camera_orientation == 0 && accelerometer_orientation == 270) {
                rotate_type = 5;
            }
            if (camera_orientation == 90 && accelerometer_orientation == 0) {
                rotate_type = 5;
            }
            if (camera_orientation == 90 && accelerometer_orientation == 90) {
                rotate_type = 2;
            }
            if (camera_orientation == 90 && accelerometer_orientation == 180) {
                rotate_type = 7;
            }
            if (camera_orientation == 90 && accelerometer_orientation == 270) {
                rotate_type = 4;
            }
            if (camera_orientation == 180 && accelerometer_orientation == 0) {
                rotate_type = 4;
            }
            if (camera_orientation == 180 && accelerometer_orientation == 90) {
                rotate_type = 5;
            }
            if (camera_orientation == 180 && accelerometer_orientation == 180) {
                rotate_type = 2;
            }
            if (camera_orientation == 180 && accelerometer_orientation == 270) {
                rotate_type = 7;
            }
            if (camera_orientation == 270 && accelerometer_orientation == 0) {
                rotate_type = 7;
            }
            if (camera_orientation == 270 && accelerometer_orientation == 90) {
                rotate_type = 4;
            }
            if (camera_orientation == 270 && accelerometer_orientation == 180) {
                rotate_type = 5;
            }
            if (camera_orientation == 270 && accelerometer_orientation == 270) {
                rotate_type = 2;
            }
        } else {
            if (final_orientation == 0) {
                rotate_type = 1;
            }
            if (final_orientation == 90) {
                rotate_type = 6;
            }
            if (final_orientation == 180) {
                rotate_type = 3;
            }
            if (final_orientation == 270) {
                rotate_type = 8;
            }
        }

        if (accelerometer_orientation == 0) {
            render_w = roi_w;
            render_h = roi_h;
            render_rotate_type = 1;
        }
        if (accelerometer_orientation == 90) {
            render_w = roi_h;
            render_h = roi_w;
            render_rotate_type = 8;
        }
        if (accelerometer_orientation == 180) {
            render_w = roi_w;
            render_h = roi_h;
            render_rotate_type = 3;
        }
        if (accelerometer_orientation == 270) {
            render_w = roi_h;
            render_h = roi_w;
            render_rotate_type = 6;
        }
    }

    // crop and rotate nv21
    cv::Mat nv21_croprotated(roi_h + roi_h / 2, roi_w, CV_8UC1);
    {
        const unsigned char *srcY = nv21 + nv21_roi_y * nv21_width + nv21_roi_x;
        unsigned char *dstY = nv21_croprotated.data;
        ncnn::kanna_rotate_c1(srcY, nv21_roi_w, nv21_roi_h, nv21_width, dstY, roi_w, roi_h, roi_w,
                              rotate_type);

        const unsigned char *srcUV =
                nv21 + nv21_width * nv21_height + nv21_roi_y * nv21_width / 2 + nv21_roi_x;
        unsigned char *dstUV = nv21_croprotated.data + roi_w * roi_h;
        ncnn::kanna_rotate_c2(srcUV, nv21_roi_w / 2, nv21_roi_h / 2, nv21_width, dstUV, roi_w / 2,
                              roi_h / 2, roi_w, rotate_type);
    }

    // nv21_croprotated to rgb
    cv::Mat rgb(roi_h, roi_w, CV_8UC3);
    ncnn::yuv420sp2rgb(nv21_croprotated.data, roi_w, roi_h, rgb.data);
    return rgb;
}


// java函数实现
// public native boolean openCamera(int facing);
JNIEXPORT jboolean JNICALL
Java_com_example_camera_1kotlin_NcnnBodypose_openCamera(JNIEnv *env, jobject thiz, jint facing) {
    if (facing < 0 || facing > 1)
        return JNI_FALSE;

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "openCamera %d", facing);

    g_camera->open((int) facing);

    return JNI_TRUE;
}

// public native boolean closeCamera();
JNIEXPORT jboolean JNICALL
Java_com_example_camera_1kotlin_NcnnBodypose_closeCamera(JNIEnv *env, jobject thiz) {
    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "closeCamera");

    g_camera->close();

    return JNI_TRUE;
}

// public native boolean setOutputWindow(Surface surface);
JNIEXPORT jboolean JNICALL
Java_com_example_camera_1kotlin_NcnnBodypose_setOutputWindow(JNIEnv *env, jobject thiz,
                                                           jobject surface) {
    ANativeWindow *win = ANativeWindow_fromSurface(env, surface);

    __android_log_print(ANDROID_LOG_DEBUG, "ncnn", "setOutputWindow %p", win);

    g_camera->set_window(win);

    return JNI_TRUE;
}
JNIEXPORT jdoubleArray JNICALL
Java_com_example_camera_1kotlin_NcnnBodypose_detectPose(JNIEnv *env, jobject thiz, jbyteArray data) {
    int len = env->GetArrayLength(data);
    unsigned char *buf = new unsigned char[len];
    env->GetByteArrayRegion(data, 0, len, reinterpret_cast<jbyte *>(buf));
    cv::Mat rgb = on_image(buf, 640, 480);

    std::vector<keypoint> points;
    g_nanodet->detect_pose(rgb, points);
    jdouble res[17 * 2];
    jdoubleArray out = env->NewDoubleArray(17 * 2);
    for (int i = 0; i < 17; i++) {
        keypoint k = points[i];
        res[i * 2] = k.x;
        res[i * 2 + 1] = k.y;
    }
    env->SetDoubleArrayRegion(out, 0, 17 * 2, res);
    return out;
}
}