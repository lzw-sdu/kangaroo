import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:get/get.dart';
import 'package:kangaroo/channel/method_channel/dance_channel.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'child_connect_card_logic.dart';

class ChildConnectCardWidget extends StatelessWidget {
  const ChildConnectCardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ChildConnectCardLogic());

    return Container(
      child: Column(
        children: [
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          Image.asset(ImageAssets.RIGHT_LOGO, height: 100.h),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          Text('连接成功！'),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          Text('请将手机放到指定位置'),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          ElevatedButton(
              onPressed: () {
                Fluttertoast.showToast(msg: "暂无可用视频");
              },
              child: Text('查看妈妈录制的引导视频')),
          ElevatedButton(
              onPressed: () {
                DanceMethodChanel().navigateToDanceUI();
                Timer(Duration(seconds: 5), () {
                  Fluttertoast.showToast(msg: "资源模块加载异常，请检查网络状态");
                });
              },
              child: Text('开启投屏')),
        ],
      ),
    );
  }
}
