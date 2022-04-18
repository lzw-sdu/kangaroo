import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';

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
              topTitle: '少儿舞蹈',
              nodeName: '初级民族舞',
              videoName: '《草原小骏马》',
              topIcon: "暂定",
              buttonAction: logic.nextPage),
          _itemCard(
              topTitle: '少儿体育',
              nodeName: '初级羽毛球',
              videoName: '《高远球接球动作》',
              topIcon: "暂定",
              buttonAction: logic.nextPage),
        ],
      ),
    );
  }

  Widget _itemCard(
      {required String topTitle,
      required String topIcon,
      required String nodeName,
      required videoName,
      required Function buttonAction}) {
    return Container(
      height: 132.h,
      width: Get.width,
      child: Card(
          child: Padding(
        padding: EdgeInsets.symmetric(horizontal: 12.w),
        child: Column(
          children: [
            Transform.translate(
              offset: Offset(0, -14.h),
              child: Row(
                children: [
                  Transform.scale(
                    origin: Offset(-26.w, 0),
                    scale: 2,
                    child: Text(
                      topTitle,
                      style: TextStyle(
                        fontSize: 14.sp,
                      ),
                    ),
                  ),
                  Padding(padding: EdgeInsets.symmetric(horizontal: 10.w)),
                  Transform.scale(
                    origin: Offset(-56.w, 0),
                    child: Icon(Icons.person),
                    scale: 2,
                  ),
                ],
              ),
            ),
            Expanded(
              child: Padding(
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
                          Text('当前练习节点：$nodeName'),
                          Row(
                            mainAxisSize: MainAxisSize.max,
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            crossAxisAlignment: CrossAxisAlignment.center,
                            children: [
                              Text(
                                videoName,
                                style: TextStyle(
                                  fontSize: 20.sp,
                                  // overflow: TextOverflow.clip,
                                ),
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
                                    },
                                  ),
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
              ),
              flex: 1,
            ),
          ],
        ),
      )),
    );
  }
}
