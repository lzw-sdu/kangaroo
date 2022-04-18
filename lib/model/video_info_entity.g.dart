// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'video_info_entity.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VideoInfoEntity _$VideoInfoEntityFromJson(Map<String, dynamic> json) =>
    VideoInfoEntity(
      json['video_id'] as String,
      json['video_url'] as String,
      json['videoSL_url'] as String,
      json['title'] as String,
      json['intro'] as String,
    );

Map<String, dynamic> _$VideoInfoEntityToJson(VideoInfoEntity instance) =>
    <String, dynamic>{
      'video_id': instance.videoId,
      'video_url': instance.videoUrl,
      'videoSL_url': instance.videoSLURL,
      'title': instance.title,
      'intro': instance.intro,
    };
