import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'parent_home_state.dart';

class ParentHomeLogic extends GetxController {
  final ParentHomeState state = ParentHomeState();

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

  void modifyDancePlanButtonAction() {
    Get.to(ChangePlan());
  }

  String nodeName = "初级羽毛球";
  String videoName = "高远球接球动作";
  String imagePath = "assets/image/test2.png";

  void modifySportPlanButtonAction() {
    Get.to(ChangePlan())?.then((value) {
     /* nodeName = "健身操";
      videoName = "帕梅拉儿童版";
      imagePath = "assets/image/test212.png";
      update();*/
    });
  }
}

class ChangePlan extends StatefulWidget {
  @override
  _ChangePlanState createState() => _ChangePlanState();
}

class _ChangePlanState extends State<ChangePlan> {
  int state = 0;

  String image = ImageAssets.CHANGE_PLAN;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("修改计划"),
      ),
      body: Center(
        child: CircularProgressIndicator(),
      ),
    );
    /*GestureDetector(
      child: SafeArea(
        child: Container(
          child: Image.asset(
            image,
            fit: BoxFit.fill,
          ),
        ),
      ),
      onTap: () => {
        setState(() {
          if (state == 0) {
            state = 1;
            image = ImageAssets.CHANGE_PLAN_CHOOSE;
          } else if (state == 1) {
            Get.back();
          }
        })
      },
    );*/
  }
}
