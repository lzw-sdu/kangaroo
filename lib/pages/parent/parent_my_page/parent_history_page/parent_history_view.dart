import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'parent_history_logic.dart';

class ParentHistoryPage extends StatelessWidget {
  ParentHistoryPage({Key? key}) : super(key: key);

  final logic = Get.put(ParentHistoryLogic());
  final state = Get.find<ParentHistoryLogic>().state;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('运动历史'),
      ),
      body: Center(
        child: Text('暂无运动历史'),
      ),
    );
  }
}
