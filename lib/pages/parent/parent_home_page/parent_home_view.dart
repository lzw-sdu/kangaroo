import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/widgets/common_card_widgets.dart';

import '../../../utils/image_assets.dart';
import '../../../widgets/common_logo_widgets.dart';
import 'parent_home_logic.dart';

class ParentHomePage extends StatelessWidget {
  const ParentHomePage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ParentHomeLogic());
    final state = Get.find<ParentHomeLogic>().state;

    return Container(
        width: Get.size.width,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [
              Get.theme.colorScheme.primaryContainer,
              Get.theme.colorScheme.primary,
            ],
            stops: [0.0, 1.0],
            begin: Alignment.topLeft,
            end: Alignment.bottomRight,
          ),
        ),
        child: Stack(
          children: [
            Positioned(
              child: CommonLogoWidgets.logoLeading(
                  width: 65.w, height: 65.h, borderWidth: 5.w),
              top: 70.h,
              left: 24.w,
            ),
            Container(
              width: Get.size.width,
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.center,
                children: [
                  Padding(padding: EdgeInsets.only(top: 90.h)),
                  Container(
                    child: Text(
                      '欢迎回来 !',
                      style: TextStyle(
                          color: Get.theme.colorScheme.onPrimary,
                          fontSize: 28.sp,
                          fontWeight: FontWeight.w600),
                    ),
                  ),
                  Container(
                    padding: EdgeInsets.symmetric(vertical: 50.h),
                    child: Text.rich(TextSpan(
                        style: TextStyle(
                          letterSpacing: 2,
                          color: Get.theme.colorScheme.onPrimary,
                        ),
                        children: [
                          TextSpan(
                            text: '25',
                            style: TextStyle(
                              // letterSpacing: 2,
                              fontSize: 80.sp,
                              fontWeight: FontWeight.w900,
                            ),
                          ),
                          TextSpan(
                              text: 'min',
                              style: TextStyle(
                                fontSize: 12.sp,
                                fontWeight: FontWeight.w800,
                              ))
                        ])),
                  ),
                  ElevatedButton(onPressed: () {}, child: Text('查看昨日运动分析报告')),
                  Padding(padding: EdgeInsets.symmetric(vertical: 12.h)),
                  Expanded(
                    child: CommonCardWidgets.bottomCard(
                        child: GetBuilder<ParentHomeLogic>(
                      builder: (logic) => Padding(
                        padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 12.h),
                        child: Container(
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
                                  topIcon: "暂定",
                                  buttonAction:
                                      logic.modifyDancePlanButtonAction),
                              _itemCard(
                                  topTitle: Image.asset(
                                    ImageAssets.CHILD_SPORT_TEXT,
                                    width: 140.w,
                                    height: 45.h,
                                  ),
                                  nodeName: '初级羽毛球',
                                  videoName: '高远球接球动作',
                                  topIcon: "暂定",
                                  buttonAction:
                                      logic.modifySportPlanButtonAction),
                            ],
                          ),
                        ),
                      ),
                    )),
                    flex: 1,
                  )
                ],
              ),
            )
          ],
        ));
  }
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
                      color: Colors.red,
                      borderRadius: BorderRadius.all(Radius.circular(4)),
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
                            onPressed: () => buttonAction,
                            child: Text(
                              '修改计划',
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
