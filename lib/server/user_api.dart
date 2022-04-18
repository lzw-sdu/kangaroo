import 'package:dio/dio.dart';
import 'package:kangaroo/common/request/http_utils.dart';
import 'package:kangaroo/model/child_info_entity.dart';

import '../model/result_entity.dart';

class UserAPI {
  static Future<ResultEntity> childLoginByUserNameAndPwd(String username, String password, String k) async {
    Map data = await HttpUtils.get(
      "/child/login",
      params: {'username': username, 'password': password, 'k': k},
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

  static Future<ResultEntity> registerParentAccount(String username, String password, String phoneNumber, String sms_code) async {
    Map data = await HttpUtils.post(
      "/parent/register",
      data: {'username': username, 'password': password, 'phoneNumber': phoneNumber, 'sms_code': sms_code},
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

  static Future<ResultEntity> registerChildAccount(String username, String password) async {
    Map data = await HttpUtils.post(
      "/parent/childRegister",
      data: {'username': username, 'password': password},
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

  static Future<ResultEntity> bindChildAccount(String username, String password) async {
    Map data = await HttpUtils.post(
      "/user/bindChild",
      data: {'username': username, 'password': password},
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

  static Future<ResultEntity> refreshLoginStatus(String refreshToken) async {
    Map data = await HttpUtils.post(
      "/parent/refresh_token",
      data: {'refreshToken': refreshToken},
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

  static Future<ResultEntity> getParentInfo() async {
    Map data = await HttpUtils.get(
      "/parent/getParentInfo",
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

  static Future<ResultEntity> getChildInfo() async {
    Map data = await HttpUtils.get(
      "/child/getChildrenInfo",
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

  static Future<ResultEntity> getChildSport() async {
    Map data = await HttpUtils.get(
      "/child/getChildrenSport",
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

  static Future<ResultEntity> uploadParentAvatar(MultipartFile avatar) async {
    Map data = await HttpUtils.post(
      "/parent/uploadAvatar",
      data: {'avatar': avatar}
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

  //TODO:没有参数
  static Future<ResultEntity> changeParentInfo() async {
    Map data = await HttpUtils.put(
        "/user/changeParentInfo",
        data: {'avatar': null}
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

  static Future<ResultEntity> changeChildInfo(ChildInfoEntity childInfoEntity) async {
    Map data = await HttpUtils.post(
        "/child/changeChildInfo",
        data: childInfoEntity.toJson(),
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

  static Future<ResultEntity> removeBindChild(String username) async {
    Map data = await HttpUtils.delete(
      "/parent/removeBindChild",
      data: {'username': username},
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

  static Future<ResultEntity> uploadChildAvatar(MultipartFile avatar) async {
    Map data = await HttpUtils.post(
      "/child/uploadAvatar",
      data: {'avatar': avatar},
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

  static Future<ResultEntity> parentLogout() async {
    Map data = await HttpUtils.delete(
      "/parent/logout",
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

  //TODO:没有参数
  static Future<ResultEntity> parentLogin() async {
    Map data = await HttpUtils.get(
      "/parent/login",
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

  static Future<ResultEntity> sendMessage(String phone) async {
    Map data = await HttpUtils.post(
      "/parent/sms",
      data: {"phone": phone}
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

  static Future<ResultEntity> childLogout() async {
    Map data = await HttpUtils.post(
        "/child/logout",
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
