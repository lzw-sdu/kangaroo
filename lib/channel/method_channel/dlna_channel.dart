import 'package:flutter/services.dart';
import 'package:get/get.dart';
import 'package:get/get_core/src/get_main.dart';

import '../../pages/child/child_bottom_cards/child_tv_search_card_widget/child_tv_search_card_logic.dart';

class DLNAChannel {
  static const String DANCE_CHANNEL = "com.sdu.kangaroo/dlna";
  static const MethodChannel _channel = const MethodChannel(DANCE_CHANNEL);
  static const MethodChannel deviceChannel =
      const MethodChannel("com.sdu.kangaroo/device");
  static const String METHOD_FIND_DEVICE = "DLNAFindDevice";
  static const String METHOD_SELECT_MODE = "DLNASelectMode";
  static const String METHOD_CONNECT = "DLNAConnect";

  static const String SCREEN_MODE = "SCREEN";

  void findDevice() {
    _channel.invokeMethod(METHOD_FIND_DEVICE);
  }

  void selectMode(String mode) {
    _channel.invokeMethod(METHOD_SELECT_MODE, mode);
  }

  void connect(String deviceName) {
    _channel.invokeMethod(METHOD_CONNECT, deviceName);
  }

/*  void receiveDevice(Function(List<String>) callback) {
    _deviceChannel.setMethodCallHandler((MethodCall call) async {
      switch (call.method) {
        case "onDeviceChanged":
          await callback(call.arguments);
          break;
        default:
          break;
      }
    });
  }*/
}
