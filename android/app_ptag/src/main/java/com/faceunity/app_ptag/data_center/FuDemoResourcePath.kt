package com.faceunity.app_ptag.data_center

import com.faceunity.pta.pta_core.data_build.FuResourcePath

/**
 * 用于参考的 [FuResourcePath] 实现类。对应方法的路径如无修改可不重写。
 */
class FuDemoResourcePath : FuResourcePath(
    ossAssets = "OSSAssets",
    appAssets = "AppAssets",
    binAssets = "pta_kit"
) {

}