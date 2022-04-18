import 'package:flutter/material.dart';
import 'package:kangaroo/utils/image_assets.dart';

class ParentMainState {
  late PageController pageController;
  late int pageIndex;
  late List<Map<String, String>> bottomNavigationBar;

  ParentMainState() {
    pageController = PageController();
    pageIndex = 0;

    bottomNavigationBar = [
      {
        'icon': ImageAssets.HOME_TRANSPARENT,
        'label': '首页',
      },
      {
        'icon': ImageAssets.KANGAROO_TRANSPARENT,
        'label': '袋鼠圈',
      },
      {
        'icon': ImageAssets.PERSON_TRANSPARENT,
        'label': '我的',
      }
    ];

    ///Initialize variables
  }
}
