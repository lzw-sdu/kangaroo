import 'package:dio/dio.dart';

import '../common/request/http_utils.dart';
import '../model/result_entity.dart';

class VideoAPI {
  static Future<ResultEntity> getVideoList() async {
    Map data = await HttpUtils.post(
      "/parent/video/list",
    ).catchError(
      (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }

  static Future<ResultEntity> parentChooseVideo(
      String username, List<int> videoIds) async {
    Map data = await HttpUtils.post(
      "/parent/video/post",
      data: {'username': username, 'video_id': videoIds},
    ).catchError(
      (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }

  static Future<ResultEntity> uploadChildOriginVideo(MultipartFile video) async {
    Map data = await HttpUtils.post(
      "/child/videoproduce/raw/post",
      data: {'video': video, 'time': DateTime.now()},
    ).catchError(
      (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }

  static Future<ResultEntity> getChildOriginVideo() async {
    Map data = await HttpUtils.post(
      "/child/videoproduce/raw/get",
    ).catchError(
          (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }

  static Future<ResultEntity> getChildCartoonVideo() async {
    Map data = await HttpUtils.post(
      "/child/videoproduce/cartoon/get",
    ).catchError(
          (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }

  static Future<ResultEntity> uploadRawVideo(MultipartFile file) async {
    Map data = await HttpUtils.post(
      "/admin/videoproduce/raw/rawpost",
    ).catchError(
          (dioError) {
        var error = dioError.error;
        return {'code': error.code, 'msg': error.message};
      },
    );
    if (data['code'] == 200) {
      return ResultEntity.success(data: data['data']);
    } else {
      return ResultEntity.error(data['msg']);
    }
  }
}
