import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';
import 'package:kangaroo/widgets/common_logo_widgets.dart';
import 'package:percent_indicator/circular_percent_indicator.dart';

import 'child_bottom_card_component/child_bottom_card_view.dart';
import 'child_bottom_cards/child_connect_card_widget/child_connect_card_view.dart';
import 'child_bottom_cards/child_sport_card_widget/child_sport_card_view.dart';
import 'child_bottom_cards/child_tv_choose_card_widget/child_tv_choose_card_view.dart';
import 'child_bottom_cards/child_tv_search_card_widget/child_tv_search_card_view.dart';
import 'child_home_logic.dart';

class ChildHomePage extends StatelessWidget {
  ChildHomePage({Key? key}) : super(key: key);

  final logic = Get.find<ChildHomeLogic>();
  final state = Get.find<ChildHomeLogic>().state;

  @override
  Widget build(BuildContext context) {
    ScreenUtil.init(
        BoxConstraints(
            maxWidth: MediaQuery.of(context).size.width,
            maxHeight: MediaQuery.of(context).size.height),
        designSize: Size(411.4, 914.3),
        context: context,
        minTextAdapt: true,
        orientation: Orientation.portrait);

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
              child: CommonLogoWidgets.logoLeading(width: 65.w, height: 65.h, borderWidth: 5.w),
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
                  Padding(padding: EdgeInsets.symmetric(vertical: 5.h)),
                  _taskProcessBar(),
                  Padding(padding: EdgeInsets.symmetric(vertical: 8.h)),
                  Text.rich(
                    TextSpan(
                        style: TextStyle(
                          color: Get.theme.colorScheme.onPrimary,
                        ),
                        children: [
                          TextSpan(
                            text: '妈妈',
                            style: TextStyle(
                              fontSize: 18.sp,
                            ),
                          ),
                          TextSpan(
                            text: '为你昨天的表现点了个大大的赞 !',
                            style: TextStyle(
                              fontSize: 15.sp,
                            ),
                          ),
                        ]),
                  ),
                  Padding(padding: EdgeInsets.symmetric(vertical: 12.h)),
                  Expanded(
                    child: ChildBottomCardComponent(children: [
                      ChildSportCardWidget(),
                      ChildTVSearchCardWidget(),
                      ChildTVChooseCardWidget(),
                      ChildConnectCardWidget(),
                    ], initPage: 0),
                    flex: 1,
                  ),
                ],
              ),
            )
          ],
        ));
  }

  Widget _taskProcessBar() {
    return Container(
        child: CircularPercentIndicator(
      radius: 100.w,
      lineWidth: 15.w,
      percent: 0.5,
      center: Container(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text.rich(TextSpan(
                style: TextStyle(
                  letterSpacing: 2,
                  color: Get.theme.colorScheme.onPrimary,
                ),
                children: [
                  TextSpan(
                    text: '50',
                    style: TextStyle(
                      // letterSpacing: 2,
                      fontSize: 60.sp,
                      fontWeight: FontWeight.w900,
                    ),
                  ),
                  TextSpan(text: '%', style: TextStyle())
                ])),
            Padding(padding: EdgeInsets.symmetric(vertical: 6.h)),
            Text(
              '已经点亮任务点2个',
              style: TextStyle(
                color: Get.theme.colorScheme.onPrimary,
                fontSize: 12.sp,
              ),
            ),
            Padding(padding: EdgeInsets.symmetric(vertical: 2.h)),
            Text(
              '体育新星',
              style: TextStyle(
                color: Get.theme.colorScheme.onPrimary,
                fontSize: 16.sp,
              ),
            ),
          ],
        ),
      ),
      progressColor: Get.theme.colorScheme.onPrimary,
      backgroundColor: Get.theme.colorScheme.onPrimary.withOpacity(0.5),
    ));
  }
}
