import 'package:get/get.dart';
import 'package:kangaroo/pages/child/child_bottom_card_component/child_bottom_card_logic.dart';

class ChildSportCardLogic extends GetxController {

  final _cardLogic = Get.find<ChildBottomCardLogic>();

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
