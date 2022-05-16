import 'dart:async';

import 'package:chewie/chewie.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';
import 'package:video_player/video_player.dart';

import 'parent_cartoon_generate_logic.dart';

class ParentCartoonGeneratePage extends StatelessWidget {
  ParentCartoonGeneratePage({Key? key}) : super(key: key);

  final logic = Get.put(ParentCartoonGenerateLogic());
  final state = Get.find<ParentCartoonGenerateLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('卡通人物视频生成'),
      ),
      body: Center(
        child: Text('暂无可生成视频'),
      ),
    );
    /*Obx(() => GestureDetector(
          child:
       */ /*   onTap: () {
            switch (state.currentIndex.value) {
              case 0:
                {
                  Get.to(ChooseImagePage());
                  state.currentIndex.value = 1;
                  Timer(Duration(seconds: 20), () {
                    state.currentIndex.value = 2;
                  });
                  break;
                }
              case 1:
                {
                  Get.to(VideoPage());
                  state.currentIndex.value = 2;
                  break;
                }
              case 2:
                {
                  Get.to(VideoPage());
                  break;
                }
            }
          },*/ /*
        ));*/
  }
}

class ChooseImagePage extends StatelessWidget {
  ChooseImagePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      child: SafeArea(
        child: Container(
          child: Image.asset(ImageAssets.IMAGE_CHOOSE, fit: BoxFit.fill),
        ),
      ),
      onTap: () => Get.back(),
    );
  }
}

class VideoPage extends StatefulWidget {
  VideoPage({Key? key}) : super(key: key);

  @override
  VideoPageState createState() => VideoPageState();
}

class VideoPageState extends State<VideoPage> {
  late VideoPlayerController _controller;
  late ChewieController _chewieController;

  @override
  void initState() {
    super.initState();
    _controller = VideoPlayerController.asset("assets/video/test.mp4")
      ..initialize().then((_) {
        // Ensure the first frame is shown after the video is initialized, even before the play button has been pressed.
        setState(() {});
      });
    _chewieController = ChewieController(
      videoPlayerController: _controller,
      autoPlay: false,
      allowFullScreen: true,
      fullScreenByDefault: true,
    );
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.landscapeRight,
      DeviceOrientation.landscapeRight,
    ]);
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      child: _controller.value.isInitialized
          ? Chewie(
              controller: _chewieController,
            )
          : Container(
              child: Text("Loading"),
            ),
    );
  }

  @override
  void dispose() {
    super.dispose();
    SystemChrome.setPreferredOrientations([
      DeviceOrientation.portraitUp,
    ]);
    _controller.dispose();
  }
}
