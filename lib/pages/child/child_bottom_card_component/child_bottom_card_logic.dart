import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'child_bottom_card_state.dart';

class ChildBottomCardLogic extends GetxController {
  final ChildBottomCardState state = ChildBottomCardState();

  @override
  void onInit() {
    super.onInit();
    state.cards = [
      Container(
        width: double.infinity,
        height: double.infinity,
        color: Colors.red,
        child: TextButton(
          child: Text('切换1'),
          onPressed: () {
            nextPage();
            debugPrint('1');
          },
        ),
      ),
      Container(
        width: double.infinity,
        height: double.infinity,
        color: Colors.blue,
        child: TextButton(
          child: Text('切换2'),
          onPressed: () {
            nextPage();
            debugPrint('2');
          },
        ),
      ),
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

  void nextPage() {
    if (state.index + 1 >= state.cards.length) {
      log('已经到达最后一页', name: 'ChildBottomCardLogic.nextPage');
      return;
    }
    state.index++;
    update();
  }

  void lastPage() {
    if (state.index - 1 < 0) {
      log('已经到达第一页', name: 'ChildBottomCardLogic.lastPage');
      return;
    }
    state.index--;
    update();
  }

  void jumpTo(int index) {
    if (index >= state.cards.length) {
      log('传入index异常', name: 'ChildBottomCardLogic.jumpTo');
      return;
    }
    state.index = index;
    update();
  }
}
