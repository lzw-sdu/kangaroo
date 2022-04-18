import 'package:flutter/material.dart';
import 'package:get/get.dart';

class Gradients {
  static final linearGradient = LinearGradient(
    colors: [
      Get.theme.colorScheme.primaryContainer,
      Get.theme.colorScheme.primary,
    ],
    stops: const [0.0, 1.0],
    begin: Alignment.topCenter,
    end: Alignment.bottomCenter,
  );
}
