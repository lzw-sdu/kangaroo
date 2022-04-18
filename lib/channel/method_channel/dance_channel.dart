import 'package:flutter/services.dart';

class DanceMethodChanel {
  static const String DANCE_CHANNEL = "com.sdu.kangaroo/dance";
  static const String METHOD_DANCE_UI = "dance_ui";

  void navigateToDanceUI() {
    MethodChannel channel = MethodChannel(DANCE_CHANNEL);
    channel.invokeMethod(METHOD_DANCE_UI);
  }
}