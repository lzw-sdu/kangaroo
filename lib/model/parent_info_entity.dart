import 'package:json_annotation/json_annotation.dart';

part 'parent_info_entity.g.dart';

@JsonSerializable()
class ParentInfoEntity {
  late String id;
  late String avatar;

  ParentInfoEntity(this.id, this.avatar);

  factory ParentInfoEntity.fromJson(Map<String, dynamic> json) =>
      _$ParentInfoEntityFromJson(json);

  Map<String, dynamic> toJson() => _$ParentInfoEntityToJson(this);
}
