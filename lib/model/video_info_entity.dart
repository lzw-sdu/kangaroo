import 'package:json_annotation/json_annotation.dart';
import 'package:json_annotation/json_annotation.dart';

part 'video_info_entity.g.dart';
@JsonSerializable(explicitToJson: true)
class VideoInfoEntity {
  @JsonKey(name: "video_id")
  late String videoId;
  @JsonKey(name: "video_url")
  late String videoUrl;
  @JsonKey(name:"videoSL_url")
  late String videoSLURL;
  late String title;
  late String intro;

  VideoInfoEntity(
      this.videoId, this.videoUrl, this.videoSLURL, this.title, this.intro);

factory VideoInfoEntity.fromJson(Map<String, dynamic> json) => _$VideoInfoEntityFromJson(json);

Map<String, dynamic> toJson() => _$VideoInfoEntityToJson(this);
}
