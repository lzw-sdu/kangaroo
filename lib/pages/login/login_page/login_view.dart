import 'dart:developer';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/style/gradient.dart';
import 'package:kangaroo/utils/image_assets.dart';

import '../sign_in_page/sign_in_view.dart';
import '../sign_up_page/sign_up_view.dart';
import 'login_logic.dart';

class LoginPage extends StatelessWidget {
  LoginPage({Key? key}) : super(key: key);

  final logic = Get.put(LoginLogic());

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: SingleChildScrollView(
      child: Container(
        height: MediaQuery.of(context).size.height,
        width: MediaQuery.of(context).size.width,
        //设置渐变的背景
        decoration: BoxDecoration(
          gradient: Gradients.linearGradient,
        ),
        child: Column(
          mainAxisSize: MainAxisSize.max,
          children: <Widget>[
            const SizedBox(
              height: 50,
            ),
            const Image(
                width: 200, height: 200, image: AssetImage(ImageAssets.LOGO)),
            Padding(
              padding: EdgeInsets.symmetric(vertical: 2),
              child: Text(
                'Kangaroo Dance',
                style: TextStyle(
                    fontSize: 24, color: Get.theme.colorScheme.primary),
              ),
            ),
            const SizedBox(
              height: 20,
            ),
            Container(
              width: logic.buttonWidth * 2,
              height: 50,
              decoration: const BoxDecoration(
                borderRadius: BorderRadius.all(Radius.circular(25)),
                color: Color(0x552B2B2B),
              ),
              child: Stack(
                children: [
                  Obx(() => Transform.translate(
                        offset: Offset(logic.pageOffset * logic.scale, 0),
                        child: Container(
                          decoration: const BoxDecoration(
                            borderRadius: BorderRadius.all(
                              Radius.circular(25),
                            ),
                            color: Colors.white,
                          ),
                          width: logic.buttonWidth,
                        ),
                      )),
                  Row(
                    children: <Widget>[
                      Expanded(
                          child: GestureDetector(
                        onTap: () {
                          logic.pageController.animateToPage(0,
                              duration: const Duration(milliseconds: 300),
                              curve: Curves.decelerate);
                        },
                        child: Container(
                          color: Colors.transparent,
                          child: Center(
                            child: const Text(
                              "登录",
                              style: TextStyle(fontSize: 16),
                            ),
                          ),
                        ),
                      )),
                      Expanded(
                          child: GestureDetector(
                        onTap: () {
                          logic.pageController.animateToPage(1,
                              duration: const Duration(milliseconds: 300),
                              curve: Curves.decelerate);
                        },
                        child: Container(
                          color: Colors.transparent,
                          child: Center(
                            child: const Text(
                              "注册",
                              style: TextStyle(fontSize: 16),
                            ),
                          ),
                        ),
                      )),
                    ],
                  ),
                ],
              ),
            ),
            Expanded(
                child: PageView(
              controller: logic.pageController,
              children: const [SignInPage(), SignUpPage()],
              onPageChanged: (index) {
                logic.currentPage.value = index;
              },
            )),
          ],
        ),
      ),
    ));
  }
}
