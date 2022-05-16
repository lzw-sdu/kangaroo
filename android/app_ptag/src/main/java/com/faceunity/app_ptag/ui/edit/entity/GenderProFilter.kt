package com.faceunity.app_ptag.ui.edit.entity

import com.faceunity.app_ptag.ui.edit.entity.control.MinorCategoryModel
import com.faceunity.app_ptag.ui.edit.entity.control.SubCategoryNormalModel
import com.faceunity.support.filter.GenderFilter

/**
 * Created on 2021/10/11 0011 16:42.


 */
class GenderProFilter(vararg keys: GenderFilterKey?) : GenderFilter(*keys) {

    override fun filter(any: Any?): GenderFilterKey? {
        var filter = super.filter(any)
        if (filter == null) {
            if (any is SubCategoryNormalModel) {
                filter = parseKey(any.useFUItem())
            } else if (any is MinorCategoryModel) {
                filter = parseKey(any.useFUMinorCategory())
            }
        }
        return filter
    }
}