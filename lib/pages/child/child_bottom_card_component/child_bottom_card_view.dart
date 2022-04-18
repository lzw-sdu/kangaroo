import 'package:animations/animations.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get/get.dart';
import 'package:kangaroo/widgets/common_card_widgets.dart';

import 'child_bottom_card_logic.dart';

class ChildBottomCardComponent extends StatelessWidget {
  // ChildBottomCardComponent({Key? key}) : super(key: key);

  ChildBottomCardComponent(
      {required List<Widget> children, int initPage = 0, Key? key})
      : super(key: key) {
    state.cards = children;
  }

  final logic = Get.put(ChildBottomCardLogic());
  final state = Get.find<ChildBottomCardLogic>().state;

  @override
  Widget build(BuildContext context) {
    return CommonCardWidgets.bottomCard(
        child: GetBuilder<ChildBottomCardLogic>(builder: (logic) {
      return Padding(
        padding: EdgeInsets.fromLTRB(12.w, 12.h, 12.w, 12.h),
        child: PageTransitionSwitcher(
          reverse: state.index % 2 == 0,
          duration: const Duration(milliseconds: 500),
          transitionBuilder: (Widget child, Animation<double> primaryAnimation,
              Animation<double> secondaryAnimation) {
            return SharedAxisTransition(
                child: child,
                animation: primaryAnimation,
                secondaryAnimation: secondaryAnimation,
                transitionType: SharedAxisTransitionType.horizontal);
          },
          child: state.cards[state.index],
        ),
      );
    }));
  }
}

class  A  extends StatefulWidget {
  const A ({Key? key}) : super(key: key);

  @override
  State<A> createState() => _State();
}

class _State extends State<A> {
  @override
  Widget build(BuildContext context) {
    WidgetsFlutterBinding.ensureInitialized();
    setState(() {

    });
    return Container();
  }
}

