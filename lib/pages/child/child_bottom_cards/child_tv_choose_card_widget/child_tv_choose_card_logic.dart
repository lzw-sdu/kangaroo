import 'package:get/get.dart';

import '../../child_bottom_card_component/child_bottom_card_logic.dart';

class ChildTVChooseCardLogic extends GetxController {
  final _cardLogic = Get.find<ChildBottomCardLogic>();

  late List<String> tvList;

  @override
  void onInit() {
    super.onInit();
    tvList = ["test1", 'test2', 'test3', 'test4', 'test5'];
  }

  @override
  void onReady() {
    // TODO: implement onReady
    super.onReady();
  }

  @override
  void onClose() {
    // TODO: implement onClose
    super.onClose();
  }

  void nextPage() => _cardLogic.nextPage();

  void lastPage() => _cardLogic.lastPage();

  void jumpTo(int index) => _cardLogic.jumpTo(index);
}
