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
    // DanceMethodChanel().navigateToDanceUI();
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

  void login(String username, String pwd) {
    if (username == "parent" && pwd == 'kangaroo') {
      // PageRoutes.addRouter(routeName: PageName.parentHome);
      Get.offAllNamed(PageName.parentHome);
    } else if (username == 'child' && pwd == 'kangaroo') {
      // PageRoutes.addRouter(routeName: PageName.childHome);
      Get.offAllNamed(PageName.childHome);
    } else {
      Fluttertoast.showToast(msg: "用户名或密码错误");
    }
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
      msg: "服务器请求异常，请稍后再试",
    );
  }

  @override
  void onClose() {
    // TODO: implement onClose
    super.onClose();
  }
}
