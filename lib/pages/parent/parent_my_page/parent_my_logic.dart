import 'package:get/get.dart';
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
      {
        'icon': ImageAssets.TRANSCRIBE_LOGO,
        'title': '录制手机摆放指南',
        'action': _jumpToGuide
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

  void _jumpToHistory() {}

  void _jumpToVideoGenerate() {}

  void _jumpToMyFavorite() {}

  void _jumpToGuide() {}
}
