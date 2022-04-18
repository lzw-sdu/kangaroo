import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'parent_main_state.dart';

class ParentMainLogic extends GetxController {
  final ParentMainState state = ParentMainState();

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

  void pageChange(index) {
    state.pageIndex = index;
    state.pageController.animateToPage(index,
        duration: Duration(milliseconds: 500), curve: Curves.decelerate);
    update();
  }
}
