import 'package:flutter/services.dart';
import 'package:get/get.dart';

import '../../../../channel/method_channel/dlna_channel.dart';
import '../../child_bottom_card_component/child_bottom_card_logic.dart';
import '../child_tv_search_card_widget/child_tv_search_card_logic.dart';

class ChildTVChooseCardLogic extends GetxController {
  final _cardLogic = Get.find<ChildBottomCardLogic>();
  final _tvSearchLogic = Get.find<ChildTVSearchCardLogic>();

  late RxList<String?> tvList = _tvSearchLogic.devices;

  @override
  void onInit() {
    super.onInit();
    tvList = _tvSearchLogic.devices;
    updateData();
    // tvList.value = ["客厅的奇异果电视", "seal", "恒星播放器"];
  }

  void updateData() {
    DLNAChannel.deviceChannel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case "onDeviceChanged":
          tvList.value = List.castFrom<Object?, String?>(call.arguments);
          break;
        default:
          break;
      }
    });
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
