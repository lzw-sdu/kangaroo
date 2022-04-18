import 'package:get/get.dart';

import 'parent_main_logic.dart';

class ParentMainBinding extends Bindings {
  @override
  void dependencies() {
    Get.lazyPut(() => ParentMainLogic());
  }
}
