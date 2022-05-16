import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kangaroo/style/gradient.dart';

import 'sign_up_logic.dart';

class SignUpPage extends StatelessWidget {
  const SignUpPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(SignUpLogic());
    final state = Get.find<SignUpLogic>().state;

    return Container(
      padding: const EdgeInsets.only(top: 23),
      child: Column(
        children: <Widget>[
          Container(
            decoration: BoxDecoration(
                borderRadius: const BorderRadius.all(Radius.circular(8)),
                color: Get.theme.colorScheme.background),
            width: 300,
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: <Widget>[
                Padding(
                  padding: const EdgeInsets.only(
                      left: 25, right: 25, top: 15, bottom: 15),
                  child: TextFormField(
                    controller: state.accountTextEditingController,
                    decoration: InputDecoration(
                        icon: Icon(
                          Icons.person_outline,
                          color: Get.theme.colorScheme.onBackground,
                        ),
                        hintText: "用户名",
                        border: InputBorder.none),
                    style: TextStyle(
                        fontSize: 16,
                        color: Get.theme.colorScheme.onBackground),
                  ),
                ),
                Container(
                  height: 1,
                  width: 250,
                  color: Colors.grey[400],
                ),
                Padding(
                  padding: const EdgeInsets.only(
                      left: 25, right: 25, top: 15, bottom: 15),
                  child: TextFormField(
                    controller: state.passwordTextEditingController,
                    decoration: InputDecoration(
                      icon: Icon(
                        Icons.lock,
                        color: Get.theme.colorScheme.onBackground,
                      ),
                      hintText: "密码",
                      border: InputBorder.none,
                      suffixIcon: IconButton(
                          icon: Icon(
                            state.isShowPwd.value
                                ? Icons.visibility_off
                                : Icons.visibility,
                            color: Get.theme.colorScheme.onBackground,
                          ),
                          onPressed: () {
                            state.isShowPwd.value = !state.isShowPwd.value;
                          }),
                    ),
                    style: TextStyle(
                        fontSize: 16,
                        color: Get.theme.colorScheme.onBackground),
                  ),
                ),
                Container(
                  height: 1,
                  width: 250,
                  color: Colors.grey[400],
                ),
                Padding(
                  padding: const EdgeInsets.only(
                      left: 25, right: 25, top: 15, bottom: 15),
                  child: TextFormField(
                    controller: state.confirmTextEditingController,
                    decoration: InputDecoration(
                        icon: Icon(
                          Icons.lock,
                          color: Get.theme.colorScheme.onBackground,
                        ),
                        suffixIcon: IconButton(
                            icon: Icon(
                              state.isShowConfirm.value
                                  ? Icons.visibility_off
                                  : Icons.visibility,
                              color: Get.theme.colorScheme.onBackground,
                            ),
                            onPressed: () {
                              state.isShowConfirm.value =
                                  !state.isShowConfirm.value;
                            }),
                        hintText: "确认密码",
                        border: InputBorder.none),
                    style: TextStyle(
                        fontSize: 16,
                        color: Get.theme.colorScheme.onBackground),
                  ),
                ),
                Container(
                  height: 1,
                  width: 250,
                  color: Colors.grey[400],
                ),
                Padding(
                  padding: const EdgeInsets.only(
                      left: 25, right: 25, top: 15, bottom: 15),
                  child: TextFormField(
                    controller: state.phoneNumberTextEditingController,
                    decoration: InputDecoration(
                        icon: Icon(
                          Icons.phone,
                          color: Get.theme.colorScheme.onBackground,
                        ),
                        hintText: "手机号",
                        border: InputBorder.none),
                    style: TextStyle(
                        fontSize: 16,
                        color: Get.theme.colorScheme.onBackground),
                  ),
                ),
                Container(
                  height: 1,
                  width: 250,
                  color: Colors.grey[400],
                ),
                Padding(
                  padding: const EdgeInsets.only(
                      left: 25, right: 25, top: 15, bottom: 0),
                  child: TextFormField(
                    controller: state.securityCodeTextEditingController,
                    decoration: InputDecoration(
                      icon: Icon(
                        Icons.phone_locked,
                        color: Get.theme.colorScheme.onBackground,
                      ),
                      hintText: "验证码",
                      border: InputBorder.none,
                      suffixIcon: TextButton(
                        onPressed: () async {
                          logic.sendSecurityCode();
                        },
                        child: GetBuilder<SignUpLogic>(
                          builder: (logic) => Text(
                            logic.state.count == 0
                                ? '获取验证码'
                                : '${logic.state.count}s',
                            style: TextStyle(
                                color: Get.theme.colorScheme.onBackground),
                          ),
                        ),
                      ),
                    ),
                    style: TextStyle(
                        fontSize: 16,
                        color: Get.theme.colorScheme.onBackground),
                  ),
                ),
                Transform.translate(
                  offset: const Offset(0, 12.5),
                  child: Transform.scale(
                    child: InkWell(
                      child: Container(
                        padding: const EdgeInsets.only(
                            left: 24, right: 24, top: 5, bottom: 5),
                        decoration: BoxDecoration(
                          borderRadius:
                              const BorderRadius.all(Radius.circular(2.5)),
                          gradient: Gradients.linearGradient,
                        ),
                        child: const Text(
                          "注册",
                          style: TextStyle(fontSize: 12.5, color: Colors.white),
                        ),
                      ),
                      onTap: () => logic.signUp(),
                    ),
                    scale: 2,
                  ),
                ),
              ],
            ),
          ),
        ],
      ),
    );
    ;
  }
}
