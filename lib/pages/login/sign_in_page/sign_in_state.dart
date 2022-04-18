import 'package:flutter/material.dart';
import 'package:get/get.dart';

class SignInState {
  late RxBool isShowPassWord;
  late RxBool usePwd;

  //验证码倒计时
  late int count;

  late TextEditingController accountTextEditingController;
  late TextEditingController passwordTextEditingController;
  late TextEditingController securityCodeTextEditingController;

  SignInState() {
    ///Initialize variables
    isShowPassWord = false.obs;
    usePwd = false.obs;
    count = 0;

    accountTextEditingController = new TextEditingController();
    passwordTextEditingController = new TextEditingController();
    securityCodeTextEditingController = new TextEditingController();
  }
}
