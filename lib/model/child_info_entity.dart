import 'package:json_annotation/json_annotation.dart';
part 'child_info_entity.g.dart';
@JsonSerializable()
class ChildInfoEntity {
  late String id;
  late String name;
  late int age;
  late int sex;
  late int weight;
  late int height;
  late String hobby;
  late String speciality;

  ChildInfoEntity(this.id, this.name, this.age, this.sex, this.weight,
      this.height, this.hobby, this.speciality);


factory ChildInfoEntity.fromJson(Map<String, dynamic> json) => _$ChildInfoEntityFromJson(json);

Map<String, dynamic> toJson() => _$ChildInfoEntityToJson(this);
}