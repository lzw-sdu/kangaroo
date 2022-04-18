import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';
import 'package:kangaroo/widgets/common_card_widgets.dart';

import '../../../widgets/common_logo_widgets.dart';
import 'parent_my_logic.dart';

class ParentMyPage extends StatelessWidget {
  const ParentMyPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ParentMyLogic());
    final state = Get.find<ParentMyLogic>().state;

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
      child: Container(
        width: Get.size.width,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              child: _topPart(),
              height: 0.35.sh,
            ),
            Expanded(
              child: CommonCardWidgets.bottomCard(
                  child: Container(
                padding: EdgeInsets.symmetric(horizontal: 12.w),
                child: Column(
                  children: [
                    Padding(padding: EdgeInsets.symmetric(vertical: 24.h)),
                    Expanded(
                      child: Row(
                        children: [
                          Expanded(
                            child: _baseInfoCard(),
                            flex: 1,
                          ),
                          Padding(
                              padding: EdgeInsets.symmetric(horizontal: 12.w)),
                          Expanded(
                            child: _sportInfoCard(),
                            flex: 1,
                          ),
                        ],
                      ),
                      flex: 1,
                    ),
                    Padding(padding: EdgeInsets.symmetric(vertical: 12.h)),
                    Expanded(
                      child: _bottomCard(state.bottomCardItems),
                      flex: 2,
                    ),
                    Padding(padding: EdgeInsets.symmetric(vertical: 5.h)),
                  ],
                ),
              )),
              flex: 1,
            ),
          ],
        ),
      ),
    );
  }

  Widget _topPart() {
    return Container(
      margin: EdgeInsets.only(left: 24.w),
      child: Stack(
        children: [
          Positioned(
            child: SizedBox(
              child: Transform.translate(
                offset: Offset(12.w, 27.h),
                child: Image.asset(ImageAssets.LOGO),
              ),
              width: 190.w,
              height: 190.h,
            ),
            bottom: 0,
            right: 0,
          ),
          Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Padding(
                padding: EdgeInsets.symmetric(vertical: 30.h),
              ),
              Row(
                mainAxisSize: MainAxisSize.max,
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  CommonLogoWidgets.logoLeading(
                      width: 55.w, height: 55.h, borderWidth: 4.w),
                  Padding(
                    padding: EdgeInsets.only(right: 16.w),
                    child: InkWell(
                      child: SizedBox(
                        height: 40.w,
                        width: 40.w,
                        child: Image.asset(
                          ImageAssets.SETTING_ICON,
                          color: Get.theme.colorScheme.onPrimary,
                        ),
                      ),
                      onTap: () {},
                    ),
                  )
                ],
              ),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.h),
              ),
              Container(
                child: Text(
                  'KangarooDance',
                  style: TextStyle(
                      color: Get.theme.colorScheme.onPrimary,
                      fontSize: 28.sp,
                      fontWeight: FontWeight.w500),
                ),
              ),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.h),
              ),
              _medalIcon(icon: Image.asset(ImageAssets.MEDAL_LOGO), text: '运动新星'),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 10.h),
              ),
              _medalIcon(icon: Image.asset(ImageAssets.MEDAL_LOGO), text: '新锐宝妈'),
              Padding(
                padding: EdgeInsets.symmetric(vertical: 25.h),
              ),
            ],
          ),
        ],
      ),
    );
  }

  Widget _medalIcon({required Widget icon, required String text}) {
    return Container(
      width: 140.w,
      decoration: BoxDecoration(
        color: Get.theme.colorScheme.tertiary.withOpacity(0.6),
        borderRadius: BorderRadius.circular(20.w),
      ),
      child: Row(
        children: [
          Padding(
            padding: EdgeInsets.only(left: 5.w),
            child: icon,
          ),
          Padding(
            padding: EdgeInsets.only(left: 12.w),
            child: Text(
              text,
              style: TextStyle(
                  fontSize: 18.sp, color: Get.theme.colorScheme.onPrimary),
            ),
          )
        ],
      ),
    );
  }

  Widget _baseCard({required Widget child}) {
    return Container(
      child: Card(
        color: Get.theme.colorScheme.onPrimary,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.all(Radius.circular(10.0)),
        ),
        child: child,
      ),
    );
  }

  Widget _baseInfoCard() {
    TextStyle _textStyle =
        TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w300);
    return _baseCard(
      child: Container(
        padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 16.h),
        child: Column(
          children: [
            Row(
              children: [
                SizedBox(
                  width: 20.w,
                  height: 20.h,
                  child: Image.asset(ImageAssets.BASE_INFO_ICON),
                ),
                Padding(padding: EdgeInsets.symmetric(horizontal: 4.w)),
                Text(
                  '基本信息',
                  style: TextStyle(fontWeight: FontWeight.w600),
                ),
              ],
            ),
            Padding(padding: EdgeInsets.symmetric(vertical: 6.h)),
            Expanded(
              child: Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                mainAxisSize: MainAxisSize.max,
                children: [
                  Column(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    mainAxisSize: MainAxisSize.max,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('年龄：9岁', style: _textStyle),
                      Text('性别：男', style: _textStyle),
                      Text('体重：40kg', style: _textStyle),
                    ],
                  ),
                  Column(
                    mainAxisAlignment: MainAxisAlignment.spaceAround,
                    mainAxisSize: MainAxisSize.max,
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text('特长：篮球', style: _textStyle),
                      Text('爱好：篮球', style: _textStyle),
                      Text('身高：133cm', style: _textStyle),
                    ],
                  ),
                ],
              ),
              flex: 1,
            )
          ],
        ),
      ),
    );
  }

  Widget _sportInfoCard() {
    TextStyle _textStyle =
        TextStyle(fontSize: 12.sp, fontWeight: FontWeight.w300);
    return _baseCard(
      child: Container(
        padding: EdgeInsets.fromLTRB(16.w, 16.h, 16.w, 16.h),
        child: Column(
          children: [
            Row(
              children: [
                SizedBox(
                  width: 20.w,
                  height: 20.h,
                  child: Image.asset(ImageAssets.BASE_INFO_ICON),
                ),
                Padding(padding: EdgeInsets.symmetric(horizontal: 4.w)),
                Text(
                  '运动信息',
                  style: TextStyle(fontWeight: FontWeight.w600),
                ),
              ],
            ),
            Padding(padding: EdgeInsets.symmetric(vertical: 6.h)),
            Expanded(
              child: Column(
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: [
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '少儿舞蹈：',
                        style: _textStyle,
                      ),
                      Expanded(
                        child: Text(
                          '民族舞初级',
                          style: _textStyle,
                        ),
                        flex: 1,
                      )
                    ],
                  ),
                  Row(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        '少儿体育：',
                        style: _textStyle,
                      ),
                      Expanded(
                        child: Text(
                          '羽毛球初级、篮球中级',
                          style: _textStyle,
                        ),
                        flex: 1,
                      )
                    ],
                  ),
                ],
              ),
              flex: 1,
            )
          ],
        ),
      ),
    );
  }

  /**
   * item内容：
   * {
   *    "icon": icon路径，
   *    "title": 标题文字,
   *    "action": item点击回调
   *  }
   */
  Widget _bottomCard(List<Map<String, dynamic>> items) {
    TextStyle _textStyle = TextStyle(fontSize: 18.sp, fontWeight: FontWeight.w200);
    return _baseCard(
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 20.h, horizontal: 28.w),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceAround,
        children:
        items.map((Map e) =>  InkWell(
          child: Row(
            children: [
              SizedBox(
                width: 45.w,
                height: 45.h,
                child: Image.asset(e['icon']),
              ),
              Padding(padding: EdgeInsets.symmetric(horizontal: 12.w)),
              Text(e['title'], style: _textStyle),
            ],
          ),
          onTap: e['action'],
        )).toList(),
      ),
    ));
  }
}
