# kangaroo（袋鼠运动）

项目采用Flutter + 原生安卓混合编写

Flutter主要负责上层UI交互，原生安卓侧负责核心功能与部分UI

原生安卓侧因项目规模庞大，故采用多模块开发提升可维护性

技术栈：Flutter + Dio + MethodChannel + GetX + CameraX + Compose + ViewModel + LiveData

目录分级：

- lib /flutter项目
  - channel 与原生通信通道
  - common 通用性能力
  - model 基础数据模块
  - pages UI部分
  - router 路由管理
  - server 服务部分
  - style 项目主题
  - utils 工具类
  - widgets 封装的widget
- Android
  - alpha_player 2D增强现实渲染器核心代码
  - camera CameraX封装 + JNI调用ncnn集成的人体姿态估计模型
  - nginxserver 原本为nginx启动nginx使用，后废弃改为RTSPClient
  - rtmplibs 视频编解码部分，用于RTMP、RTSP推流
  - screening 屏幕处理+DLNA服务核心模块
  - screenrecorder 屏幕录制核心代码
  - webserver 基于nanohtttpd实现WebServer
  - healthy OPPO HealthySDK
  - app_ptag 增强现实镜像舞伴模块
  - simplepermission 工具模块，用于权限申请
  - dance 核心运动模块UI，Compose绘制
  - provider ICDF 近场通信生产者
  - wear 手表端项目模块
  - pose_score 动作评分模块

项目规模：（包含部分github开源代码）

![image-20220422153401504](https://markdown-1305004560.cos.ap-beijing.myqcloud.com/uPic/image-20220422153401504.png)
