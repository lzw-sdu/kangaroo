import 'package:get/get.dart';
import 'package:kangaroo/channel/method_channel/dance_channel.dart';
import 'package:kangaroo/pages/parent/parent_my_page/parent_cartoon_generate_page/parent_cartoon_generate_view.dart';
import 'package:kangaroo/pages/parent/parent_my_page/parent_history_page/parent_history_view.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'parent_my_state.dart';

class ParentMyLogic extends GetxController {
  final ParentMyState state = ParentMyState();

  @override
  void onInit() {
    super.onInit();
    state.bottomCardItems = [
      {
        'icon': ImageAssets.HISTORY_LOGO,
        'title': '运动历史',
        'action': _jumpToHistory
      },
      {
        'icon': ImageAssets.VIDEO_LOGO,
        'title': '趣味视频生成工具',
        'action': _jumpToVideoGenerate
      },
      {
        'icon': ImageAssets.FAVORITE_LOGO,
        'title': '我的收藏',
        'action': _jumpToMyFavorite
      },
      // {
      //   'icon': ImageAssets.TRANSCRIBE_LOGO,
      //   'title': '录制手机摆放指南',
      //   'action': _jumpToGuide
      // },
      {
        'icon': ImageAssets.TRANSCRIBE_LOGO,
        'title': '镜像卡通人物舞伴',
        'action': _jumpToMirror
      },
    ];
  }

  @override
  void onReady() {
    // TODO: implement onReady
    super.onReady();
  }

  @override
  void onClose() {
    // TODO: implement onClose
    super.onClose();
  }

  void _jumpToHistory() {
    Get.to(ParentHistoryPage());
  }

  void _jumpToVideoGenerate() {
    Get.to(ParentCartoonGeneratePage());
  }

  void _jumpToMyFavorite() {}

  void _jumpToGuide() {}

  void _jumpToMirror() {
    DanceMethodChanel().navigateToMirrorUI();
  }
}
