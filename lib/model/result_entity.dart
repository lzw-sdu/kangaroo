class ResultEntity<T> {
  late int code;
  late String message;
  late T? data;

  ResultEntity({required this.code, required this.message, this.data});

  ResultEntity.success({T? data}) {
    this.code = 200;
    this.message = "success";
    this.data = data;
  }

  ResultEntity.error(String msg, {int code = 500, T? data}) {
    this.code = code;
    this.message = msg;
    this.data = data;
  }
}
