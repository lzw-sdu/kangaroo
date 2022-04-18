//
// Created by 26864 on 2022/3/14.
//


#ifndef NANODET_H
#define NANODET_H

#include <opencv2/core/core.hpp>

#include <net.h>
struct keypoint3d
{
    float x;
    float y;
    float z;
    float score;
};


class Threedpose {
public:
    Threedpose();

    int load(const char* modeltype, int target_size, const float* mean_vals, const float* norm_vals, bool use_gpu = false);

    int load(AAssetManager* mgr, const char* modeltype, int target_size, const float* mean_vals, const float* norm_vals, bool use_gpu = false);

    int detect(const cv::Mat& rgb);

    int draw(cv::Mat& rgb);

private:

    void detect_pose(cv::Mat &rgb, std::vector<keypoint3d> &points);
    ncnn::Net poseNet;
    int feature_size;
    float kpt_scale;
    int target_size;
    float mean_vals[4];
    float norm_vals[4];
    std::vector<std::vector<float>> dist_y, dist_x;
    ncnn::UnlockedPoolAllocator blob_pool_allocator;
    ncnn::PoolAllocator workspace_pool_allocator;
};


#endif //NCNN_ANDROID_MOVENET_MAIN_THREEDPOSE_H
