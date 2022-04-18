import 'dart:async';

import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';

import 'sign_up_state.dart';

class SignUpLogic extends GetxController {
  final SignUpState state = SignUpState();

  @override
  void onReady() {
    // TODO: implement onReady
    super.onReady();
  }

  void _decreaseCount() {
    Timer.periodic(const Duration(seconds: 1), (timer) {
      if (state.count > 0) {
        state.count--;
      } else {
        timer.cancel();
      }
      update();
    });
  }

  Future sendSecurityCode() async {
    if (state.count != 0) {
      return;
    }
    state.count = 60;
    update();
    //TODO: 发送验证码
    _decreaseCount();
    Fluttertoast.showToast(
      msg: "验证码已发送",
    );
  }

  @override
  void onClose() {
    // TODO: implement onClose
    super.onClose();
  }
}
