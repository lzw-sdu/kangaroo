import 'package:get/get.dart';
import 'package:kangaroo/pages/child/child_bottom_card_component/child_bottom_card_logic.dart';

import 'child_bottom_cards/child_sport_card_widget/child_sport_card_logic.dart';
import 'child_home_logic.dart';

class ChildHomeBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => ChildHomeLogic());
    Get.lazyPut(() => ChildBottomCardLogic());

    Get.lazyPut(() => ChildSportCardLogic());
  }
}
