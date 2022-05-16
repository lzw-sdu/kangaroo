package com.faceunity.app_ptag.ui.edit.entity.facepup

import com.faceunity.fupta.facepup.entity.tier.FacepupGeneralTier
import com.faceunity.fupta.facepup.entity.tier.FacepupSlider
import com.faceunity.fupta.facepup.util.expand.equalsDelta

/**
 * 一个适用用实际业务 UI 的自定义捏脸模型。
 */
data class CustomFacepupModel(
    val group: FacepupGeneralTier.Group,
    val partList: List<CustomPart>
) {

    fun reset() {
        partList.forEach {
            it.reset()
        }
    }

    data class CustomPart(
        val part: FacepupGeneralTier.Part,
        val name: String,
        val iconPath: List<String>,
        val sliderList: List<CustomSlider>
    ) : ChangeCounter {

        fun isFilter(): Boolean {
            if (sliderList.size <= 4) {
                return false
            } else {
                return true
            }
        }

        fun filterSlider(typeList: List<String>): List<CustomSlider> {
            if (!isFilter()) return sliderList
            return sliderList.filter { typeList.contains(it.slider.type) }
        }

        fun getIconPath(isMale: Boolean): String {
            if (iconPath.size == 0) return ""
            if (iconPath.size == 1) return iconPath[0] //如果只配置了一个则返回仅有的
            if (isMale) {
                return iconPath[0]
            } else {
                return iconPath[1]
            }
        }

        override fun changeNum(): Int {
            return sumChangeNum(sliderList)
        }

        override fun reset() {
            return resetAll(sliderList)
        }

        fun isChanged(typeList: List<String>) : Boolean {
            filterSlider(typeList).forEach {
                if (it.changeNum() > 0) {
                    return true
                }
            }
            return false
        }

        fun isChanged() =  changeNum() > 0
    }

    /**
     * 自定义的滑条。
     * 接入者应该参考该模型实现自己的 Slider，然后持有相芯的 FacepupSlider 对象，然后直接传递 FacepupSlider 调用相关接口，无需关心细节。
     */
    data class CustomSlider(
        val slider: FacepupSlider,
        val name: String,
        /**
         * 滑条所处的位置，0.0 到 1.0
         */
        var location: Float
    ) : ChangeCounter {

        override fun changeNum() = if (location.equalsDelta(0.5f)) 0 else 1
        override fun reset() {
            location = 0.5f
        }

        fun isDisable() = slider.isDisable
    }

    /**
     * 对滑条数据的分类
     */
    enum class ControlMode {
        Move, Rotate, Scale;

        fun sliderType(): List<String> {
            return when(this) {
                //TODO cache
                Move -> listOf("tx", "ty", "tz")
                Rotate -> listOf("rx", "ry", "rz")
                Scale -> listOf("sx", "sy", "sz")
            }
        }

        fun showText(): String {
            return when(this) {
                Move -> "偏移"
                Rotate -> "旋转"
                Scale -> "缩放"
            }
        }
    }

    /**
     * 修改数量计数器。
     */
    interface ChangeCounter {
        /**
         * 得到当前修改过的数量。
         */
        fun changeNum() : Int

        /**
         * 将所有记录的值重置。
         * 注意：该方法仅重置数据，并不会影响渲染效果。
         */
        fun reset()

        /**
         * 供内部使用的递归重置。
         */
        fun resetAll(collection: Collection<ChangeCounter>) {
            collection.forEach { it.reset() }
        }

        /**
         * 供内部使用的递归计算修改数量。
         */
        fun sumChangeNum(collection: Collection<ChangeCounter>) : Int {
            var sum = 0
            collection.forEach {
                sum += it.changeNum()
            }
            return sum
        }
    }
}