import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

import '../../../../utils/image_assets.dart';
import 'child_sport_card_logic.dart';

class ChildSportCardWidget extends StatelessWidget {
  const ChildSportCardWidget({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.find<ChildSportCardLogic>();
    return Container(
      color: Get.theme.colorScheme.surface,
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: [
          _itemCard(
              topTitle: Image.asset(
                ImageAssets.CHILD_DANCE_TEXT,
                width: 130.w,
                height: 35.h,
              ),
              nodeName: '初级民族舞',
              videoName: '《草原小骏马》',
              topIcon: "assets/image/test1.png",
              buttonAction: logic.nextPage),
          _itemCard(
              topTitle: Image.asset(
                ImageAssets.CHILD_SPORT_TEXT,
                width: 140.w,
                height: 45.h,
              ),
              nodeName: '健身操',
              videoName: '帕梅拉儿童版',
              topIcon: "assets/image/test212.png",
              buttonAction: logic.nextPage),
        ],
      ),
    );
  }

  Widget _itemCard(
      {required Widget topTitle,
      required String topIcon,
      required String nodeName,
      required videoName,
      required Function buttonAction}) {
    return Container(
      height: 150.h,
      width: Get.width,
      child: Card(
          child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Transform.translate(
                offset: Offset(0, -24.h),
                child: Container(
                  child: Transform.scale(
                    origin: Offset(-40.w, 0),
                    scale: 2,
                    child: topTitle,
                  ),
                )),
            Expanded(
              child: Container(),
              flex: 1,
            ),
            Padding(
              padding: EdgeInsets.fromLTRB(0, 8.h, 0, 15.h),
              child: Row(
                children: [
                  Expanded(
                    child: Container(
                      decoration: BoxDecoration(
                        borderRadius: BorderRadius.all(Radius.circular(4)),
                      ),
                      child: Image.asset(
                        topIcon,
                        fit: BoxFit.fill,
                      ),
                    ),
                    flex: 1,
                  ),
                  Padding(
                    padding: EdgeInsets.symmetric(horizontal: 8.w),
                  ),
                  Expanded(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      mainAxisSize: MainAxisSize.max,
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          '当前练习节点：$nodeName',
                          style: TextStyle(
                            fontSize: 12.sp,
                            // overflow: TextOverflow.clip,
                          ),
                        ),
                        Row(
                          mainAxisSize: MainAxisSize.max,
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: [
                            Expanded(
                              child: Text(
                                videoName,
                                style: TextStyle(
                                  fontSize: 18.sp,
                                  // overflow: TextOverflow.clip,
                                ),
                              ),
                              flex: 1,
                            ),
                            ElevatedButton(
                              onPressed: () => buttonAction(),
                              child: Text(
                                '继续练习',
                                style: TextStyle(
                                    color: Get.theme.colorScheme.onTertiary),
                              ),
                              style: ButtonStyle(
                                backgroundColor:
                                    MaterialStateProperty.resolveWith<Color?>(
                                        (Set<MaterialState> states) {
                                  return Get.theme.colorScheme.tertiary;
                                }),
                              ),
                            ),
                          ],
                        )
                      ],
                    ),
                    flex: 3,
                  ),
                ],
              ),
            )
          ],
        ),
      )),
    );
  }
}
