package com.faceunity.app_ptag.ui.edit.weight.control

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.faceunity.app_ptag.databinding.ViewControlAvatarBinding
import com.faceunity.app_ptag.ui.edit.entity.control.*
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.MasterCategoryAdapter
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.MinorCategoryAdapter
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.StyleRecordInterface
import com.faceunity.app_ptag.ui.edit.weight.control.adapter.SubCategoryAdapter
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.visible
import com.faceunity.support.filter.FilterGroup

/**
 * Created on 2021/7/22 0022 16:17.


 */
class AvatarControlView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr), AvatarControlEventListener {

    private val binding: ViewControlAvatarBinding
    var controlListener: AvatarControlListener? = null
    private var filterGroup = FilterGroup()

    private var masterCategoryList: MutableList<MasterCategoryModel>? = null
    private var minorCategoryList: MutableList<MinorCategoryModel>? = null
    private var subCategoryList: MutableList<SubCategoryModel>? = null

    private val masterAdapter = MasterCategoryAdapter(this)
    private val minorAdapter = MinorCategoryAdapter(this)
    private val subAdapter = SubCategoryAdapter(this)

    init {
        binding = ViewControlAvatarBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        binding.masterCategoryView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = masterAdapter
        }
        binding.minorCategoryView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = minorAdapter
            (itemAnimator as? SimpleItemAnimator)?.apply {
                addDuration = 0
                changeDuration = 0
                moveDuration = 0
                removeDuration = 0
                supportsChangeAnimations = false

            }
        }
        binding.subCategoryView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = subAdapter
            (itemAnimator as? SimpleItemAnimator)?.apply {
                addDuration = 0
                changeDuration = 0
                moveDuration = 0
                removeDuration = 0
                supportsChangeAnimations = false

            }
        }
        binding.historyBack.apply {
            setOnClickListener {
                controlListener?.onHistoryBackClick()
            }
        }
        binding.historyForward.setOnClickListener {
            controlListener?.onHistoryForwardClick()
        }
        binding.historyReset.setOnClickListener {
            controlListener?.onHistoryResetClick()
        }
        binding.facepupBtn.setOnClickListener {
            val currentMinorCategory = minorCategoryList?.getOrNull(minorAdapter.getNowSelectPosition()) ?: return@setOnClickListener
            val fileId = subCategoryList?.getOrNull(subAdapter.getNowSelectPosition())?.let {
                it as? SubCategoryNormalModel
            }?.useFUItem()?.fuEditBundleItem?.fileID
            controlListener?.onFacepupClick(currentMinorCategory.key, fileId)
        }
    }

    /**
     * 绑定 [ControlModel] 数据模型，并进行渲染。
     */
    fun bindData(aeModel: ControlModel) {
        with(aeModel) {
            masterCategoryList = aeModel.masterList
            val index = masterAdapter.getDefaultPosition(true)
            masterCategoryList?.getOrNull(index)?.let {
                onMasterClick(it, index)
            }
        }
    }

    /**
     * 根据 [ControlModel] 进行增量数据刷新
     */
    fun updateData(aeModel: ControlModel) {
        with(aeModel) {
            masterCategoryList = aeModel.masterList
            minorCategoryList = masterCategoryList?.getOrNull(masterAdapter.getDefaultPosition())?.minorList
            val minorCategoryModel = minorCategoryList?.getOrNull(minorAdapter.getNowSelectPosition())
            subCategoryList = filterGroup.filter(minorCategoryModel?.subList)

            minorCategoryModel?.getSelectedItem()?.let {
                val selectSubIndex = subCategoryList?.indexOf(it)
                subAdapter.recordClickSafe(selectSubIndex)
                subAdapter.notifyClickItemChanged(subAdapter)
            }

            masterAdapter.submitList(masterCategoryList)
            minorAdapter.submitList(minorCategoryList)
            subAdapter.submitList(subCategoryList)

        }
    }

    /**
     * 根据传入的筛选规则，显示符合筛选条件的数据
     */
    fun filterGenderList(inputFilterGroup: FilterGroup) {
        filterGroup = inputFilterGroup
        val minorCategoryModel = minorCategoryList?.getOrNull(minorAdapter.getNowSelectPosition())
        subCategoryList = filterGroup.filter(minorCategoryModel?.subList)

        minorCategoryModel?.getSelectedItem()?.let {
            val selectSubIndex = subCategoryList?.indexOf(it)
            subAdapter.recordClickSafe(selectSubIndex)
            subAdapter.notifyClickItemChanged(subAdapter)
        }
        subAdapter.submitList(subCategoryList)
    }

    override fun onMasterClick(item: MasterCategoryModel, position: Int) {
        updateMasterEvent(item, position)
    }

    override fun onMinorClick(item: MinorCategoryModel, position: Int) {
        updateMinorEvent(item, position)

        minorAdapter.recordClickSafe(position)
        minorAdapter.notifyClickItemChanged(minorAdapter)

        controlListener?.onMinorSelect(item.useFUMinorCategory())

        binding.subCategoryView.apply {
            scrollToPosition(0)
            (layoutManager as? LinearLayoutManager)?.scrollToPositionWithOffset(0, 0)
        }
    }

    override fun onSubClick(item: SubCategoryModel, position: Int) {
        updateSubEvent(item, position)

        minorCategoryList?.getOrNull(minorAdapter.getNowSelectPosition())?.setSelectedItem(item)
        when(item) {
            is SubCategoryNormalModel -> {
                controlListener?.onNormalItemClick(item.useFUItem())
            }
            is SubCategoryColorModel -> {
                controlListener?.onColorItemClick(item.useFUColorItem())
            }
        }

    }

    private fun updateMasterEvent(item: MasterCategoryModel, position: Int) {
        masterAdapter.apply {
            submitList(masterCategoryList?.toList())
            recordClickSafe(position)
            notifyClickItemChanged(masterAdapter)
        }

        minorAdapter.tintColor = item.tintColor()
        subAdapter.tintColor = item.tintColor()
        minorAdapter.resetClickRecord()
        minorCategoryList = filterGroup.filter(item.minorList)
        val index = minorAdapter.getDefaultPosition(true)
        minorCategoryList?.getOrNull(index)?.let {
            updateMinorEvent(it, index)
            controlListener?.onMinorSelect(it.useFUMinorCategory())
        }
    }

    private fun updateMinorEvent(item: MinorCategoryModel, position: Int) {
        minorAdapter.apply {
            submitList(minorCategoryList?.toList())
//            recordClickSafe(minorCategoryList?.indexOf(item))
//            notifyClickItemChanged(minorAdapter)
        }

        subCategoryList = filterGroup.filter(item.subList)
        subAdapter.resetClickRecord()
        val selectSubIndex = item.getSelectedItem()?.let {
            subCategoryList?.indexOf(it)
        }
        if (selectSubIndex != null) {
            subAdapter.submitList(subCategoryList?.toList())
            subAdapter.recordClick(selectSubIndex)
//            subAdapter.notifyClickItemChanged(subAdapter)
        } else {
            subAdapter.submitList(subCategoryList?.toList())
        }

    }

    private fun updateSubEvent(item: SubCategoryModel, position: Int) {
        subAdapter.apply {
            submitList(subCategoryList?.toList())
            recordClickSafe(position)
            notifyClickItemChanged(subAdapter)
        }
    }

    fun notifySelectItem(selectItemList: List<String>) {
        val needNotifyIndexList = mutableListOf<Int>().apply {
            add(subAdapter.getNowSelectPosition())
            add(subAdapter.getLastSelectPosition())
        }
        val selectIndexList = mutableListOf<Int>()
        subCategoryList?.forEachIndexed { index, subCategoryModel ->
            if (subCategoryModel is SubCategoryNormalModel && subCategoryModel.useFUItem().fuEditBundleItem?.fileID in selectItemList) {
                selectIndexList.add(index)
            }
        }
        selectIndexList.forEach {
            subAdapter.recordClick(it)
            subAdapter.notifyItemChanged(it)
        }
        if (selectIndexList.isEmpty()) {
            subAdapter.recordClick(-1)
        }
        needNotifyIndexList.forEach {
            subAdapter.notifyItemChanged(it)
        }

    }

    fun controlStyleRecord(action: (styleRecordInterface: StyleRecordInterface) -> Unit) {
        action(subAdapter)
        subAdapter.notifyDataSetChanged()
    }

    /**
     * 显示编辑菜单
     */
    fun visibleControl() {
        binding.masterCategoryView.visible()
        binding.minorCategoryView.visible()
        binding.subCategoryView.visible()
        binding.historyBack.visible()
        binding.historyForward.visible()
        binding.historyReset.visible()
    }

    /**
     * 隐藏编辑菜单
     */
    fun goneControl() {
        binding.masterCategoryView.gone()
        binding.minorCategoryView.gone()
        binding.subCategoryView.gone()
        binding.facepupBtn.gone()
        binding.historyBack.gone()
        binding.historyForward.gone()
        binding.historyReset.gone()
    }

    fun visibleFacepupBtn() {
        binding.facepupBtn.visible()
    }

    fun goneFacepupBtn() {
        binding.facepupBtn.gone()
    }
}