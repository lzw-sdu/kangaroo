import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:kangaroo/pages/parent/parent_my_page/parent_cartoon_generate_page/parent_cartoon_generate_view.dart';
import 'package:kangaroo/utils/image_assets.dart';

import 'parent_kangaroo_logic.dart';

class ParentKangarooPage extends StatelessWidget {
  const ParentKangarooPage({Key? key}) : super(key: key);

  @override
  Widget build(BuildContext context) {
    final logic = Get.put(ParentKangarooLogic());
    final state = Get.find<ParentKangarooLogic>().state;

    return GestureDetector(
      child: SafeArea(
        child: Container(
            child: Center(
          child: CircularProgressIndicator(),
        )),
      ),
      // onTap: () => Get.to(ParentCartoonGeneratePage()),
    );
  }
}

class ParentKangarooDetailPage extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Container(
      child: Image.asset(
        ImageAssets.ARTICLE_DETAILS,
        fit: BoxFit.fill,
      ),
    );
  }
}
