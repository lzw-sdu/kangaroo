import 'package:get/get.dart';
import 'package:kangaroo/utils/image_assets.dart';

class ParentCartoonGenerateState {
  RxInt currentIndex = 0.obs;
  List<String> images = [
    ImageAssets.CARTOON_VIDEO,
    ImageAssets.CARTOON_VIDEO_GENERATE,
    ImageAssets.CARTOON_VIDEO_GENERATED
  ];

  ParentCartoonGenerateState() {
    ///Initialize variables
  }
}
