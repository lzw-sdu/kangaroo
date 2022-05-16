package com.faceunity.app_ptag.ui.edit.weight.facepup

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.ItemControlGroupSeekBarBinding
import com.faceunity.app_ptag.databinding.ItemTierGroupBinding
import com.faceunity.app_ptag.databinding.ItemTierTypeBinding
import com.faceunity.app_ptag.databinding.ViewControlGroupFacepupBinding
import com.faceunity.app_ptag.ui.edit.entity.facepup.CustomFacepupModel
import com.faceunity.app_ptag.util.FastAdapter
import com.faceunity.app_ptag.util.ToastUtils
import com.faceunity.app_ptag.weight.slider.Slider
import com.faceunity.editor_ptag.util.gone
import com.faceunity.editor_ptag.util.visible
import com.faceunity.editor_ptag.util.visibleOrGone
import com.faceunity.editor_ptag.util.visibleOrInvisible
import com.faceunity.fupta.facepup.entity.tier.FacepupSlider
import com.faceunity.toolbox.media.FUMediaUtils

/**
 *
 */
class FacepupControlView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewControlGroupFacepupBinding

    private var group: CustomFacepupModel? = null
    private var fileId: String? = null
    private var isMale: Boolean = true

    init {
        binding = ViewControlGroupFacepupBinding.inflate(LayoutInflater.from(context), this, true)
        initAdapter()
        initView()
    }

    private var eventListener: EventListener = object : EventListener {

        override fun onItemClick(part: CustomFacepupModel.CustomPart, index: Int) {
            val list = CustomFacepupModel.ControlMode.values().toMutableList()
            itemTypeAdapter.updateList(list)

            binding.itemTypeRecyclerView.visibleOrGone(part.isFilter())
            part.getIconPath(isMale).let {
                if (it.isBlank()) {
                    "icon_drawer_eye"
                } else {
                    it
                }
            }.let {
                FUMediaUtils.loadBitmap(context, "AppAssets/facepup_icon/${it}.png")
            }.let {
                binding.icon.setImageBitmap(it)
            }

            status.clickItem(index)
            onItemTypeClick(list[status.itemTypeIndex], status.itemTypeIndex)
            itemAdapter.notifyDataSetChanged()
        }

        override fun onItemTypeClick(
            itemType: CustomFacepupModel.ControlMode,
            index: Int
        ) {
            status.clickItemType(index)
            val part = group!!.partList[status.itemIndex]
            seekBarAdapter.updateList(part.filterSlider(itemType.sliderType()).toMutableList())
            itemTypeAdapter.notifyDataSetChanged()
        }

        override fun onItemTypeLongClick(
            itemType: CustomFacepupModel.ControlMode,
            index: Int
        ) {
//            itemType.second.seekBarItemList.forEach {
//                it.value = 0.5f
//                controlListener?.onSeekBarScroll(it, 0.5f)
//            }
//            seekBarAdapter.updateList(itemType.second.seekBarItemList)
            itemTypeAdapter.notifyDataSetChanged()
        }

        override fun onSeekBarScroll(item: CustomFacepupModel.CustomSlider, scale: Float, index: Int) {
            controlListener?.onSeekBarScroll(item.slider, scale)
        }
    }
    private var controlListener: ControlListener? = null

    private lateinit var itemAdapter: FastAdapter<CustomFacepupModel.CustomPart>
    private lateinit var itemTypeAdapter: FastAdapter<CustomFacepupModel.ControlMode>
    private lateinit var seekBarAdapter: FastAdapter<CustomFacepupModel.CustomSlider>

    private val status = Status()

    private fun initView() {

        binding.partRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemAdapter
        }
        binding.itemTypeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemTypeAdapter
        }
        binding.seekBarRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = seekBarAdapter
        }

        binding.resetBtn.setOnClickListener {
            group?.reset()
            controlListener?.onResetClick()
            itemAdapter.notifyDataSetChanged()
            itemTypeAdapter.notifyDataSetChanged()
            seekBarAdapter.notifyDataSetChanged()
        }
        binding.finishBtn.setOnClickListener {
            controlListener?.onFinish()
        }
        binding.modeBtn0.setOnClickListener {
            controlListener?.onModeSwitch(0)
            syncFacepupMode(0)
        }
        binding.modeBtn1.setOnClickListener {
            controlListener?.onModeSwitch(1)
            syncFacepupMode(1)
        }


    }

    private fun initAdapter() {
        itemAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_group) { holder, bean, position ->
            val binding = ItemTierGroupBinding.bind(holder.itemView)
            binding.name.text = bean.name
            binding.dot.visibleOrInvisible(bean.isChanged())
            if (status.itemIndex == position) {
                binding.name.setTextColor(Color.parseColor("#27272B"))
            } else {
                binding.name.setTextColor(Color.parseColor("#A7AABF"))
            }
            binding.root.setOnClickListener {
                eventListener.onItemClick(bean, position)
            }
        }
        itemTypeAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_type) { holder, bean, position ->
            val binding = ItemTierTypeBinding.bind(holder.itemView)
            binding.name.text = bean.showText()
            val part = group!!.partList[status.itemIndex]
            binding.dot.visibleOrInvisible(part.isChanged(bean.sliderType()))
            if (status.itemTypeIndex == position) {
//                binding.name.setTextColor(Color.parseColor("#27272B"))
                binding.name.setBackgroundResource(R.drawable.bg_item_type_single)
            } else {
//                binding.name.setTextColor(Color.parseColor("#A7AABF"))
                binding.name.background = null
            }
            binding.root.setOnClickListener {
                eventListener.onItemTypeClick(bean, position)
            }
            binding.root.setOnLongClickListener {
                eventListener.onItemTypeLongClick(bean, position)
                true
            }
        }
        seekBarAdapter = FastAdapter(mutableListOf(), R.layout.item_control_group_seek_bar) { holder, bean, position ->
            val binding = ItemControlGroupSeekBarBinding.bind(holder.itemView)
            if (!bean.isDisable()) {
                binding.name.setTextColor(Color.parseColor("#646778"))
                binding.value.setTextColor(Color.parseColor("#646778"))
            } else {
                binding.name.setTextColor(Color.parseColor("#C5C8DA"))
                binding.value.setTextColor(Color.parseColor("#C5C8DA"))
            }
            binding.name.text = bean.name
            binding.seekBar.apply {
                value = bean.location * (valueTo - valueFrom) + valueFrom
                binding.value.text = formatValue(value)
                isEnabled = !bean.isDisable()
                setCustomThumbDrawable(R.drawable.seekbar_thumb)
                clearOnChangeListeners()
                setOnTouchListener { v, event ->
                    if (!isEnabled) {
                        ToastUtils.showFailureToast(context, "该维度不可调整")
                    }
                    super.onTouchEvent(event)
                }
                addOnChangeListener { _, value, isUser ->

                    if (isUser) {
                        updateSeekbarValue(binding, bean, value, position)
                    }
                }
            }
            binding.minusBtn.setOnClickListener {
                if (bean.isDisable()) return@setOnClickListener
                val value = (binding.seekBar.value - 0.1f).let {
                    if (it < binding.seekBar.valueFrom) {
                        binding.seekBar.valueFrom
                    } else {
                        it
                    }
                }
                updateSeekbarValue(binding, bean, value, position)
            }
            binding.plusBtn.setOnClickListener {
                if (bean.isDisable()) return@setOnClickListener
                val value = (binding.seekBar.value + 0.1f).let {
                    if (it > binding.seekBar.valueTo) {
                        binding.seekBar.valueTo
                    } else {
                        it
                    }
                }
                updateSeekbarValue(binding, bean, value, position)
            }
        }
    }

    private fun updateSeekbarValue(
        binding: ItemControlGroupSeekBarBinding,
        bean: CustomFacepupModel.CustomSlider,
        value: Float,
        position: Int
    ) {
        val scale = (value - binding.seekBar.valueFrom) / (binding.seekBar.valueTo - binding.seekBar.valueFrom)
        eventListener.onSeekBarScroll(bean, scale, position)
        bean.location = scale
        binding.seekBar.value = value
        binding.value.text = binding.seekBar.formatValue(value)

        //及时刷新修改标识。不推荐这样做。
        itemAdapter.notifyDataSetChanged()
        itemTypeAdapter.notifyDataSetChanged()
    }

    fun setControlListener(listener: ControlListener) {
        controlListener = listener
    }

    fun syncGroupInfo(group: CustomFacepupModel, fileId: String) {
        this.group = group
        this.fileId = fileId

        itemAdapter.updateList(group.partList.toMutableList())
        status.clickItem(0)
        eventListener.onItemClick(group.partList[0], 0)

        controlListener?.onStart()
    }

    fun syncGender(gender: String) {
        isMale = gender == "male"
    }

    fun syncFacepupMode(mode: Int) {
        when(mode) {
            0 -> {
                binding.modeBtn0.isSelected = true
                binding.modeBtn1.isSelected = false
                binding.itemTypeRecyclerView.gone()
            }
            1 -> {
                binding.modeBtn1.isSelected = true
                binding.modeBtn0.isSelected = false
                binding.itemTypeRecyclerView.visible()
            }
        }
    }

    fun getFileId(): String {
        return fileId!!
    }

    fun getGroupKey(): String {
        return group?.group?.groupKey ?: ""
    }

    data class Status(var itemIndex: Int = 0, var itemTypeIndex: Int = 0) {

        fun clickItem(index: Int) {
            itemIndex = index
            itemTypeIndex = 0
        }

        fun clickItemType(index: Int) {
            itemTypeIndex = index
        }


    }

    /**
     * 事件监听，对子 View 的事件响应进行处理
     */
    internal interface EventListener {

        fun onItemClick(item: CustomFacepupModel.CustomPart, index: Int)

        fun onItemTypeClick(itemType: CustomFacepupModel.ControlMode, index: Int)

        fun onItemTypeLongClick(
            itemType: CustomFacepupModel.ControlMode,
            index: Int
        )

        fun onSeekBarScroll(item: CustomFacepupModel.CustomSlider, scale: Float, index: Int)
    }

    /**
     * 封装给外界的接口
     */
    interface ControlListener {
        fun onSeekBarScroll(slider: FacepupSlider, scale: Float)

        fun onResetClick()

        fun onStart()

        fun onFinish()

        fun onModeSwitch(mode: Int)
    }
}