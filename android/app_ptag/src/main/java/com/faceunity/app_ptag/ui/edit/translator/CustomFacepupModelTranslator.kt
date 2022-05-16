package com.faceunity.app_ptag.ui.edit.translator

import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupIcon
import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupModel
import com.faceunity.fupta.facepup.FacePupManager
import com.faceunity.fupta.facepup.entity.origin.FacepupMeshTranslation
import com.faceunity.fupta.facepup.entity.tier.FacepupGeneralTier

/**
 *
 */
class CustomFacepupModelTranslator {

    fun buildCustom(
        generalTierGroup: FacepupGeneralTier.Group,
        translation: FacepupMeshTranslation,
        facepupMap: Map<String, Float>,
        customIcon: CustomFacepupIcon
    ): CustomFacepupModel {
        val partList = mutableListOf<CustomFacepupModel.CustomPart>()
        generalTierGroup.partList.forEach { part ->
            val partKey = part.partKey
            val sliderList = mutableListOf<CustomFacepupModel.CustomSlider>()

            part.sliderList.forEach { slider ->
                val sliderValue = FacePupManager.facePupParamValueToScale(
                    slider.controlItem.first().let { facepupMap[it] ?: 0f },
                    slider.controlItem.second().let { facepupMap[it] ?: 0f }
                )
                val sliderName = translation.tranSlider(slider.type, partKey)
                sliderList.add(CustomFacepupModel.CustomSlider(slider, sliderName, sliderValue))
            }
            partList.add(CustomFacepupModel.CustomPart(part, translation.tran(partKey), customIcon.iconMap[partKey] ?: emptyList(), sliderList))
        }

        return CustomFacepupModel(generalTierGroup, partList)
    }
}