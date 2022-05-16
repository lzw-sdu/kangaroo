import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/channel/method_channel/dlna_channel.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'child_tv_search_card_logic.dart';

class ChildTVSearchCardWidget extends StatelessWidget {
  const ChildTVSearchCardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ChildTVSearchCardLogic());

/*    DLNAChannel.deviceChannel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case "onDeviceChanged":
          logic.devices.value = List.castFrom<Object?, String?>(call.arguments);
          logic.jumpTo(2);
          break;
        default:
          break;
      }
    });*/

    Timer(Duration(seconds: 4), () {
      logic.jumpTo(2);
    });

    return Container(
      child: Column(
        children: [
          Text(
            '正在搜索网络中的智能电视',
            style: TextStyle(fontSize: 26.sp),
          ),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 30.h),
          ),
          Image.asset(ImageAssets.DANCE_LOGO),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 30.h),
          ),
          Text('请稍后......'),
        ],
      ),
    );
  }
}
