import '../common/request/http_utils.dart';
import '../model/result_entity.dart';

class SportAPI {
  static Future<ResultEntity> childUploadSportInfo() async {
    Map data = await HttpUtils.post(
      "/sports/info/post",
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