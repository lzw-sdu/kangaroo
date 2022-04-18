// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'child_info_entity.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ChildInfoEntity _$ChildInfoEntityFromJson(Map<String, dynamic> json) =>
    ChildInfoEntity(
      json['id'] as String,
      json['name'] as String,
      json['age'] as int,
      json['sex'] as int,
      json['weight'] as int,
      json['height'] as int,
      json['hobby'] as String,
      json['speciality'] as String,
    );

Map<String, dynamic> _$ChildInfoEntityToJson(ChildInfoEntity instance) =>
    <String, dynamic>{
      'id': instance.id,
      'name': instance.name,
      'age': instance.age,
      'sex': instance.sex,
      'weight': instance.weight,
      'height': instance.height,
      'hobby': instance.hobby,
      'speciality': instance.speciality,
    };
