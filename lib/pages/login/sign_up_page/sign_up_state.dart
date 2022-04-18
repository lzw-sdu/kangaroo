import 'package:flutter/material.dart';
import 'package:get/get_rx/src/rx_types/rx_types.dart';

class SignUpState {

  late TextEditingController accountTextEditingController;
  late TextEditingController passwordTextEditingController;
  late TextEditingController confirmTextEditingController;
  late TextEditingController phoneNumberTextEditingController;
  late TextEditingController securityCodeTextEditingController;

  //验证码倒计时
  late int count;

  RxBool isShowPwd = false.obs;
  RxBool isShowConfirm = false.obs;

  SignUpState() {
    ///Initialize variables
    count = 0;

    accountTextEditingController = new TextEditingController();
    passwordTextEditingController = new TextEditingController();
    confirmTextEditingController = new TextEditingController();
    phoneNumberTextEditingController = new TextEditingController();
    securityCodeTextEditingController = new TextEditingController();

  }
}
