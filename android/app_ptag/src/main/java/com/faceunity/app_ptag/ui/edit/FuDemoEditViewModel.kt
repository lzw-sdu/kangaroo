package com.faceunity.app_ptag.ui.edit

import androidx.lifecycle.ViewModel
import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupIcon
import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupModel
import com.faceunity.app_ptag.ui.edit.translator.CustomFacepupModelTranslator
import com.faceunity.editor_ptag.data_center.FuDevDataCenter
import com.faceunity.editor_ptag.store.DevAvatarManagerRepository
import com.faceunity.fupta.facepup.FacePupManager
import com.faceunity.fupta.facepup.FacePupParser
import com.faceunity.fupta.facepup.entity.origin.FacepupMeshTranslation
import com.faceunity.fupta.facepup.entity.tier.FacepupGeneralTier
import com.faceunity.fupta.facepup.translator.FacepupGeneralTierTranslator

/**
 *
 */
class FuDemoEditViewModel : ViewModel() {
    var facepupMode = 0

    private lateinit var meshLanguage: FacepupMeshTranslation
    private lateinit var generalTier: FacepupGeneralTier
    private lateinit var generalSimpleTier: FacepupGeneralTier
    private lateinit var customIcon: CustomFacepupIcon

    fun initFacepup() {
        //加载原始 JSON 数据
        val meshPointsString = FuDevDataCenter.fastLoadString { appFacepupPoints() }
        val meshConfigString = FuDevDataCenter.fastLoadString { appFacepupConfig() }
        val meshConfigSimpleString = FuDevDataCenter.fastLoadString { appCustom("facepup/MeshSimpleConfig.json") }
        val meshLanguageString = FuDevDataCenter.fastLoadString { appFacepupLanguage() }
        //解析为原始数据结构
        val meshPoints = FacePupParser.parserMeshPoints(meshPointsString)
        val meshConfig = FacePupParser.parserMeshConfig(meshConfigString)
        val meshSimpleConfig = FacePupParser.parserMeshConfig(meshConfigSimpleString)
        meshLanguage = FacePupParser.parserMeshTranslation(meshLanguageString)
        //构造带层级的数据模型。根据不同的 MeshConfig 会有不同的体现，对应简单专家两个模式。
        generalTier = FacepupGeneralTierTranslator().buildFacePupGeneralTier(
            meshPoints,
            meshConfig
        )
        generalSimpleTier = FacepupGeneralTierTranslator().buildFacePupGeneralTier(
            meshPoints,
            meshSimpleConfig
        )
        //解析图标数据
        val iconString = FuDevDataCenter.fastLoadString { appCustom("facepup/facepup_icon.json") }
        customIcon = CustomFacepupIcon.buildFromJson(iconString)
    }

    /**
     * 根据数据构造一个符合业务的捏脸模型。
     * 该代码未做缓存，仅作参考。
     * @param mode 0 为简易模式、1 为专家模式
     */
    fun buildCustomGroupNotCache(groupKey: String, mode: Int) : CustomFacepupModel {
        //构建原始数据模型
        val meshPointsString = FuDevDataCenter.fastLoadString { appFacepupPoints() }
        val meshConfigString = when (mode) {
            0 -> FuDevDataCenter.fastLoadString { appCustom("facepup/MeshSimpleConfig.json") }
            else -> FuDevDataCenter.fastLoadString { appFacepupConfig() }
        }
        val meshLanguageString = FuDevDataCenter.fastLoadString { appFacepupLanguage() }
        val facePupContainer = FacePupManager.loadFacePupContainer(meshPointsString, meshConfigString, meshLanguageString)
        //构建层级数据模型
        val generalTier = FacePupManager.buildGeneralTier(facePupContainer)

        val iconString = FuDevDataCenter.fastLoadString { appCustom("facepup/facepup_icon.json") }
        val generalTierGroup = generalTier.groupList.firstOrNull { it.groupKey == groupKey }
        val translation = FacePupParser.parserMeshTranslation(meshLanguageString)
        val customGroup = CustomFacepupModelTranslator().buildCustom(
            generalTierGroup = generalTierGroup!!,
            translation = translation,
            facepupMap = DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.deformation?.getDeformationCache() ?: emptyMap(),
            customIcon = CustomFacepupIcon.buildFromJson(iconString)
        )

        return customGroup
    }


    /**
     * 根据数据构造一个符合业务的捏脸模型。
     * @param mode 0 为简易模式、1 为专家模式
     */
    @JvmOverloads
    fun buildCustomGroup(groupKey: String, mode: Int = facepupMode) : CustomFacepupModel {
        facepupMode = mode
        val generalTierGroup = when(mode) {
            0 -> generalSimpleTier
            else -> generalTier
        }.groupList.firstOrNull { it.groupKey == groupKey }
        val facepupMap =
            DevAvatarManagerRepository.getCurrentAvatarInfo()?.avatar?.deformation?.getDeformationCache() ?: emptyMap()

        val customGroup = CustomFacepupModelTranslator().buildCustom(
            generalTierGroup = generalTierGroup!!,
            translation = meshLanguage,
            facepupMap = facepupMap,
            customIcon = customIcon
        )
        return customGroup
    }
}