import 'package:flutter/material.dart';
import 'package:get/get.dart';

import 'parent_kangaroo_logic.dart';

class ParentKangarooPage extends StatelessWidget {
  const ParentKangarooPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ParentKangarooLogic());
    final state = Get.find<ParentKangarooLogic>().state;

    return Container();
  }
}
