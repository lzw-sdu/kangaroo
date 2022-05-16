package com.faceunity.app_ptag.ui.edit.entity

import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryNormalModel
import com.faceunity.support.filter.StyleFilter

/**
 * Created on 2021/10/11 0011 16:47.


 */
class StyleProFilter(vararg keys: StyleFilterKey?) : StyleFilter(*keys) {

    override fun filter(any: Any?): StyleFilterKey? {
        var filter = super.filter(any)
        if (filter == null) {
            if (any is SubCategoryNormalModel) {
                filter = parseKey(any.useFUItem())
            }
        }
        return filter
    }
}