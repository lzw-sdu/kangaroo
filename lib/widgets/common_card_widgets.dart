import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

class CommonCardWidgets {
  static Widget bottomCard({required Widget child}) {
    return Container(
      width: Get.width,
      decoration: BoxDecoration(
        color: Get.theme.colorScheme.surface,
        borderRadius: BorderRadius.only(
          topLeft: Radius.circular(24.w),
          topRight: Radius.circular(24.w),
        ),
        boxShadow: [
          BoxShadow(
              color: Get.theme.colorScheme.tertiary,
              offset: Offset(0.0, -1), //阴影y轴偏移量
              blurRadius: 10, //阴影模糊程度
              spreadRadius: 3 //阴影扩散程度
          ),
        ],
      ),
      child: child,
    );
  }
}