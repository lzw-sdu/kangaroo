import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

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
          Icon(Icons.check),
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
          ElevatedButton(onPressed: () {}, child: Text('查看妈妈录制的引导视频')),
        ],
      ),
    );
  }
}
