package com.faceunity.app_ptag.ui.edit.entity.facepup

import com.google.gson.Gson

/**
 * 捏脸模块的图标模型。
 * 长度为 2 则 index 0 为男性图标，index 1 为女性图标。
 * 长度为 1 则统一图标。
 */
class CustomFacepupIcon(
    val iconMap: Map<String, List<String>>
) {

    companion object {

        @Deprecated("临时的测试数据，应该使用 buildFromJson 从 Json 中解析")
        fun buildTestInfo(): CustomFacepupIcon {
            val map = HashMap<String, List<String>>()
            //脸
            map["forehead_jnt"] = listOf("icon_drawer_male_forehead", "icon_drawer_female_forehead")
            map["ophryon_jnt"] = listOf("icon_male_drawer_ophryon", "icon_drawer_female_ophryon")
            map["LR_malar_jnt"] = listOf("icon_drawer_male_temple", "icon_drawer_female_temple")
            map["LR_cheek_jnt"] = listOf("icon_drawer_male_cheekbone", "icon_drawer_female_cheekbone")
            map["LR_cheekUp_jnt"] = listOf("icon_drawer_male_cheek", "icon_drawer_female_cheek")
            map["LR_levator_jnt"] = listOf("icon_drawer_male_masseter", "icon_drawer_female_masseter") //咬肌
            map["chin_jnt"] = listOf("icon_drawer_male_chin_tip", "icon_drawer_female_chin_tip") //下巴尖
            map["LR_chin_jnt"] = listOf("icon_drawer_male_chin", "icon_drawer_female_chin") //下巴
            map["LR_mandible_jnt"] = listOf("icon_drawer_male_mandible", "icon_drawer_female_mandible")
            map["LR_mandibleCape_jnt"] = listOf("icon_drawer_male_mandibular_angle", "icon_drawer_female_mandibular_angle")
            map["mouth_root_jnt"] = listOf("icon_drawer_male_lower_face", "icon_drawer_female_lower_face")

            //鼻子
            map["nose_jnt"] = listOf("icon_drawer_nose")
            map["noseBridgeUp_jnt"] = listOf("icon_drawer_root")
            map["noseBridge_jnt"] = listOf("icon_drawer_bridge")
            map["LR_philtrum_jnt"] = listOf("icon_drawer_side_of_nose")
            map["LR_noseWing_jnt"] = listOf("icon_drawer_nosewing")
            map["noseHead_jnt"] = listOf("icon_drawer_head_of_nose")
            map["noseBottom_jnt"] = listOf("icon_drawer_base_of_nose")

            //眉毛
            map["LR_brow_jnt"] = listOf("icon_drawer_eyebrow")
            map["LR_innBrow_jnt"] = listOf("icon_drawer_eyebrow(1)")
            map["LR_midBrow_jnt"] = listOf("icon_drawer_eyebrow(2)")
            map["LR_outBrow_jnt"] = listOf("icon_drawer_eyebrow(3)")

            //眼睛
            map["LR_eye_jnt"] = listOf("icon_drawer_eye")
            map["LR_interEyeLid_jnt"] = listOf("icon_drawer_eye_inner")
            map["LR_outerEyeLid_jnt"] = listOf("icon_drawer_eye_outside")
            map["LR_upperEyeLidIn_jnt"] = listOf("icon_drawer_upper_eyelid_inner")
            map["LR_upperEyeLid_jnt"] = listOf("icon_drawer_upper_eyelid_mid")
            map["LR_upperEyeLidOut_jnt"] = listOf("icon_drawer_upper_eyelid_outside")
            map["LR_lowerEyeLidIn_jnt"] = listOf("icon_drawer_lower_eyelid_inner")
            map["LR_lowerEyeLid_jnt"] = listOf("icon_drawer_lower_eyelid_mid")
            map["LR_lowerEyeLidOut_jnt"] = listOf("icon_drawer_lower_eyelid_outside")
            map["LR_pupil_jnt"] = listOf("icon_drawer_lower_eyelid_inner(1)")

            //嘴巴
            map["mouth_jnt"] = listOf("icon_drawer_mouth")
            map["mouthDn_jnt"] = listOf("icon_drawer_mentolabial_sulcus")
            map["UL_Lip_jnt"] = listOf("icon_drawer_middle_lip_up")
            map["upperLip_jnt"] = listOf("icon_drawer_middle_lip_up")
            map["lowerLip_jnt"] = listOf("icon_drawer_middle_lip_down")
            map["LR_upperLip_jnt"] = listOf("icon_drawer_upper_lip")
            map["LR_lowerLip_jnt"] = listOf("icon_drawer_lower_lip")
            map["LR_lipDown_jnt"] = listOf("icon_drawer_corner_of_mouth_small")
            map["LR_lipInner_jnt"] = listOf("icon_drawer_corner_of_mouth")

            return CustomFacepupIcon(map)
        }

        fun buildTempJson(): String {
            val info = buildTestInfo()
            val json = Gson().toJson(info)
            return json
        }

        fun buildFromJson(json: String): CustomFacepupIcon {
            return Gson().fromJson<CustomFacepupIcon>(json, CustomFacepupIcon::class.java)
        }
    }
}