import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../utils/image_assets.dart';

class CommonLogoWidgets {
  static Widget logoLeading({
    required double width,
    required double height,
    double? borderWidth,
  }) =>
      Container(
        width: width,
        height: height,
        padding: EdgeInsets.all(2.w),
/*        decoration: BoxDecoration(
          shape: BoxShape.circle,
          border: Border.all(
              width: borderWidth ?? 5.w,
              color: Get.theme.colorScheme.onPrimary),
        ),*/
        child: Image.asset(
          ImageAssets.LOGO_RIGHT,
          fit: BoxFit.fill,
        ),
      );
}
