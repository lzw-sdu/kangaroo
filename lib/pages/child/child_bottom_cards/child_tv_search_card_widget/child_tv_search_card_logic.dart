import 'package:get/get.dart';

import '../../child_bottom_card_component/child_bottom_card_logic.dart';

class ChildTVSearchCardLogic extends GetxController {
  final _cardLogic = Get.find<ChildBottomCardLogic>();
  RxList<String?> devices = <String?>[].obs;

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

  void searchDevice() {}

  void nextPage() => _cardLogic.nextPage();

  void lastPage() => _cardLogic.lastPage();

  void jumpTo(int index) => _cardLogic.jumpTo(index);
}
