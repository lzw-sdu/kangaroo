import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_easyloading/flutter_easyloading.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/channel/method_channel/dlna_channel.dart';

import 'child_tv_choose_card_logic.dart';

class ChildTVChooseCardWidget extends StatelessWidget {
  const ChildTVChooseCardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ChildTVChooseCardLogic());
    return Container(
      padding: EdgeInsets.symmetric(vertical: 12.h, horizontal: 18.w),
      height: double.infinity,
      child: Column(
        children: [
          Text(
            '请选择希望连接的智能设备',
            style: TextStyle(
              fontSize: 28.sp,
            ),
          ),
          Padding(padding: EdgeInsets.symmetric(vertical: 3.h)),
          Container(
            height: 5.h,
            width: 40.w,
            decoration: BoxDecoration(
              color: Colors.orange,
              borderRadius: BorderRadius.all(Radius.circular(4)),
            ),
          ),
          SizedBox(
              height: 350.h,
              child: Obx(
                () => ListView.builder(
                  itemBuilder: (context, pos) {
                    return pos == 0
                        ? _deviceItemFirst(logic, pos)
                        : _deviceItemOther(logic, pos);
                  },
                  itemExtent: 60.h,
                  itemCount: logic.tvList.length,
                ),
              )),
          TextButton(
            child: Text('取消'),
            onPressed: () => logic.lastPage(),
          )
        ],
      ),
    );
  }

  Widget _deviceItemFirst(ChildTVChooseCardLogic logic, int pos) {
    return InkWell(
      child: Row(
        children: [
          Container(
            width: 10.w,
            height: 10.w,
            decoration: BoxDecoration(
              color: Colors.orange,
              shape: BoxShape.circle,
            ),
          ),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 4.w),
          ),
          Text(
            '${logic.tvList[pos]}（妈妈指定）',
            style: TextStyle(
              fontSize: 16.sp,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
      onTap: () {
        DLNAChannel().selectMode(DLNAChannel.SCREEN_MODE);
        EasyLoading.show(status: '正在连接...');
        Timer(Duration(seconds: 3), () {
          EasyLoading.dismiss();
          logic.nextPage();
          DLNAChannel().connect(logic.tvList[pos]!);
        });
      },
    );
  }

  Widget _deviceItemOther(ChildTVChooseCardLogic logic, int pos) {
    return InkWell(
      child: Row(
        children: [
          Container(
            width: 10.w,
            height: 10.w,
            decoration: BoxDecoration(
              color: Colors.blue,
              shape: BoxShape.circle,
            ),
          ),
          Padding(
            padding: EdgeInsets.symmetric(horizontal: 4.w),
          ),
          Text(
            '${logic.tvList[pos]}',
            style: TextStyle(
              fontSize: 14.sp,
              // fontWeight: FontWeight.w600,
            ),
          ),
        ],
      ),
      onTap: () {
        DLNAChannel().selectMode(DLNAChannel.SCREEN_MODE);
        EasyLoading.show(status: '正在连接...');
        Timer(Duration(seconds: 3), () {
          EasyLoading.dismiss();
          logic.nextPage();
          DLNAChannel().connect(logic.tvList[pos]!);
        });
      },
    );
  }
}
