import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import 'child_tv_search_card_logic.dart';

class ChildTVSearchCardWidget extends StatelessWidget {
  const ChildTVSearchCardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ChildTVSearchCardLogic());

    return Container(
      child: Column(
        children: [
          Text('正在搜索网络中的智能电视'),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          Icon(Icons.person),
          Padding(
            padding: EdgeInsets.symmetric(vertical: 10.h),
          ),
          Text('请稍后......'),
        ],
      ),
    );
  }
}
