import 'dart:async';

import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:kangaroo/channel/method_channel/dance_channel.dart';
import 'package:kangaroo/router/index.dart';

import 'sign_in_state.dart';

class SignInLogic extends GetxController {
  final SignInState state = SignInState();

  bool _isChild = false;

  @override
  void onReady() {
    // TODO: implement onReady
    super.onReady();
  }

  void showPassWord() {
    state.isShowPassWord.value = !state.isShowPassWord.value;
    DanceMethodChanel().navigateToDanceUI();
  }

  void modifyUsePwd() {
    //清空输入框内容
    state.securityCodeTextEditingController.clear();
    state.passwordTextEditingController.clear();

    state.usePwd.value = !state.usePwd.value;
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

  void login() {
    PageRoutes.addRouter(
        routeName: _isChild ? PageName.childHome : PageName.parentHome);
  }

  void forgetPwd() {
    _isChild = !_isChild;
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
