import 'package:flutter/material.dart';
import 'package:flutter_svg/svg.dart';
import 'package:get/get.dart';
import 'package:kangaroo/style/gradient.dart';
import 'sign_in_logic.dart';

class SignInPage extends StatelessWidget {
  const SignInPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(SignInLogic());
    final state = Get.find<SignInLogic>().state;

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
                    //验证
                    validator: (value) {
                      if (value!.isEmpty) {
                        return "Email can not be empty!";
                      }
                    },
                    onSaved: (value) {},
                  ),
                ),
                Container(
                  height: 1,
                  width: 250,
                  color: Colors.grey[400],
                ),

                ///usePwd 判断是否是密码登录
                Obx(() => state.usePwd.value
                    ? Padding(
                        padding:
                            const EdgeInsets.only(left: 25, right: 25, top: 15),
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
                                  state.isShowPassWord.value
                                      ? Icons.visibility_off
                                      : Icons.visibility,
                                  color: Get.theme.colorScheme.onBackground,
                                ),
                                onPressed: logic.showPassWord),
                          ),
                          obscureText: !state.isShowPassWord.value,
                          style: TextStyle(
                              fontSize: 16,
                              color: Get.theme.colorScheme.onBackground),
                        ),
                      )
                    : Padding(
                        padding: const EdgeInsets.only(
                            left: 25, right: 25, top: 15, bottom: 15),
                        child: TextFormField(
                          controller: state.securityCodeTextEditingController,
                          decoration: InputDecoration(
                            icon: Icon(
                              Icons.lock,
                              color: Get.theme.colorScheme.onBackground,
                            ),
                            hintText: "验证码",
                            border: InputBorder.none,
                            suffixIcon: TextButton(
                              onPressed: () async {
                                logic.sendSecurityCode();
                              },
                              child: GetBuilder<SignInLogic>(
                                builder: (logic) => Text(
                                  logic.state.count == 0
                                      ? '获取验证码'
                                      : '${logic.state.count}s',
                                  style: TextStyle(
                                      color:
                                          Get.theme.colorScheme.onBackground),
                                ),
                              ),
                            ),
                          ),
                          obscureText: !state.isShowPassWord.value,
                          style: TextStyle(
                              fontSize: 16,
                              color: Get.theme.colorScheme.onBackground),
                          validator: (value) {
                            if (value == null ||
                                value.isEmpty ||
                                value.length < 6) {
                              return "Password's must longer than 6!";
                            }
                          },
                          onSaved: (value) {},
                        ),
                      )),
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
                          "登录",
                          style: TextStyle(fontSize: 12.5, color: Colors.white),
                        ),
                      ),
                      onTap: () => logic.login(
                        state.accountTextEditingController.text,
                        state.passwordTextEditingController.text
                      ),
                    ),
                    scale: 2,
                  ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 40),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              mainAxisSize: MainAxisSize.max,
              children: [
                GestureDetector(
                    child: Text(
                      '忘记密码',
                      style: TextStyle(
                        fontSize: 16,
                        color: Colors.white,
                      ),
                    ),
                    onTap: () => logic.forgetPwd()),
                Container(
                  width: 1,
                  height: 21,
                  margin: const EdgeInsets.fromLTRB(10, 0, 10, 0),
                  color: Colors.white,
                ),
                Obx(
                  () => state.usePwd.value
                      ? GestureDetector(
                          onTap: () => logic.modifyUsePwd(),
                          child: const Text(
                            '验证码登录',
                            style: TextStyle(
                              fontSize: 16,
                              color: Colors.white,
                            ),
                          ),
                        )
                      : GestureDetector(
                          onTap: () => logic.modifyUsePwd(),
                          child: const Text(
                            '密码登录',
                            style: TextStyle(
                              fontSize: 16,
                              color: Colors.white,
                            ),
                          ),
                        ),
                ),
              ],
            ),
          ),
          Padding(
            padding: const EdgeInsets.only(top: 15),
            child: Row(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Container(
                  height: 1,
                  width: 100,
                  decoration: const BoxDecoration(
                      gradient: LinearGradient(colors: [
                    Colors.white10,
                    Colors.white,
                  ])),
                ),
                const Padding(
                  padding: EdgeInsets.only(left: 15, right: 15),
                  child: Text(
                    "第三方登录方式",
                    style: TextStyle(fontSize: 16, color: Colors.white),
                  ),
                ),
                Container(
                  height: 1,
                  width: 100,
                  decoration: const BoxDecoration(
                      gradient: LinearGradient(colors: [
                    Colors.white,
                    Colors.white10,
                  ])),
                ),
              ],
            ),
          ),
          const SizedBox(
            height: 10,
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.center,
            children: <Widget>[
              Container(
                padding: const EdgeInsets.all(10),
                decoration: const BoxDecoration(
                  shape: BoxShape.circle,
                  color: Colors.white,
                ),
                child: Image.asset('assets/qq.jpg', width: 40, height: 40),
              ),
              const SizedBox(
                width: 40,
              ),
              Container(
                padding: EdgeInsets.all(10),
                decoration: const BoxDecoration(
                  shape: BoxShape.circle,
                  color: Colors.white,
                ),
                child: SvgPicture.asset('assets/svg/weixin.svg',
                    width: 40, height: 40),
              ),
            ],
          )
        ],
      ),
    );
  }
}
