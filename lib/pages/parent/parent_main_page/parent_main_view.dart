import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/pages/parent/parent_home_page/parent_home_view.dart';
import 'package:kangaroo/pages/parent/parent_kangaroo_page/parent_kangaroo_view.dart';
import 'package:kangaroo/pages/parent/parent_my_page/parent_my_view.dart';

import '../../../utils/image_assets.dart';
import 'parent_main_logic.dart';

class ParentMainPage extends StatelessWidget {
  ParentMainPage({Key? key}) : super(key: key);

  final logic = Get.find<ParentMainLogic>();
  final state = Get.find<ParentMainLogic>().state;

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

    return Scaffold(
      body: GetBuilder<ParentMainLogic>(
        builder: (logic) => PageView(
          controller: state.pageController,
          children: [ParentHomePage(), ParentKangarooPage(), ParentMyPage()],
        ),
      ),
      bottomNavigationBar: GetBuilder<ParentMainLogic>(
        builder: (logic) => BottomNavigationBar(
          onTap: (index) => logic.pageChange(index),
          currentIndex: state.pageIndex,
          backgroundColor: Get.theme.colorScheme.surface,
          items: state.bottomNavigationBar
              .map(
                (e) => BottomNavigationBarItem(
                  icon: SizedBox(
                    width: 40.w,
                    height: 40.h,
                    child: Image.asset(
                      e['icon']!,
                      color: Get.theme.colorScheme.surfaceVariant,
                    ),
                  ),
                  activeIcon: SizedBox(
                    width: 40.w,
                    height: 40.h,
                    child: Image.asset(
                      e['icon']!,
                    ),
                  ),
                  label: e['label'],
                ),
              )
              .toList(),
        ),
      ),
    );
  }
}
