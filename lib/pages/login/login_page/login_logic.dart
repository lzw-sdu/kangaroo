import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:get/get.dart';

class LoginLogic extends GetxController {
  late PageController pageController;
  late PageView pageView;
  RxInt currentPage = 0.obs;

  double buttonWidth = 150;
  RxDouble pageOffset = 0.0.obs;
  double scale = 0;

  @override
  void onInit() {
    pageController = PageController();
    scale = buttonWidth / Get.size.width;
    pageController.addListener(() {
      pageOffset.value = pageController.offset;
    });
    super.onInit();
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
}
