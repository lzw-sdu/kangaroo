package com.faceunity.fupta.facepup.weight

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.ItemTierGroupBinding
import com.faceunity.app_ptag.databinding.ItemTierSeekBarBinding
import com.faceunity.app_ptag.databinding.ViewControlTierFacepupBinding
import com.faceunity.app_ptag.util.FastAdapter
import com.faceunity.fupta.facepup.entity.FacePupTierWrapper
import kotlin.math.roundToInt

/**
 *
 */
class FacePupTierControlView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    data class Status(var groupIndex: Int = 0, var itemIndex: Int = 0, var itemTypeIndex: Int = 0) {
        fun clickGroup(index: Int) {
            groupIndex = index
            itemIndex = 0
            itemTypeIndex = 0
        }

        fun clickItem(index: Int) {
            itemIndex = index
            itemTypeIndex = 0
        }

        fun clickItemType(index: Int) {
            itemTypeIndex = index
        }


    }

    private val binding: ViewControlTierFacepupBinding
    private var eventListener: FacePupTierEventListener = object : FacePupTierEventListener {
        override fun onGroupClick(group: FacePupTierWrapper.Group, index: Int) {
            itemAdapter.updateList(group.itemList)

            status.clickGroup(index)
            onItemClick(group.itemList[status.itemIndex], status.itemIndex)
            groupAdapter.notifyDataSetChanged()
        }

        override fun onItemClick(item: FacePupTierWrapper.Item, index: Int) {
            val list = item.groupMap.map { Pair(it.key, it.value) }.toMutableList()
            itemTypeAdapter.updateList(list)

            status.clickItem(index)
            onItemTypeClick(list[status.itemTypeIndex], status.itemTypeIndex)
            itemAdapter.notifyDataSetChanged()
        }

        override fun onItemTypeClick(itemType: Pair<FacePupTierWrapper.ControlMode, FacePupTierWrapper.SeekBarGroup>, index: Int) {
            status.clickItemType(index)
            seekBarAdapter.updateList(itemType.second.seekBarItemList)
            itemTypeAdapter.notifyDataSetChanged()
        }

        override fun onItemTypeLongClick(itemType: Pair<FacePupTierWrapper.ControlMode, FacePupTierWrapper.SeekBarGroup>, index: Int) {
            itemType.second.seekBarItemList.forEach {
                it.value = 0.5f
                controlListener?.onSeekBarScroll(it, 0.5f)
            }
            seekBarAdapter.updateList(itemType.second.seekBarItemList)
            itemTypeAdapter.notifyDataSetChanged()
        }

        override fun onSeekBarScroll(item: FacePupTierWrapper.SeekBarItem, scale: Float, index: Int) {
            controlListener?.onSeekBarScroll(item, scale)
        }
    }
    private var controlListener: FacePupControlTierListener? = null

    private var facePupTierWrapper: FacePupTierWrapper? = null
    private lateinit var groupAdapter: FastAdapter<FacePupTierWrapper.Group>
    private lateinit var itemAdapter: FastAdapter<FacePupTierWrapper.Item>
    private lateinit var itemTypeAdapter: FastAdapter<Pair<FacePupTierWrapper.ControlMode, FacePupTierWrapper.SeekBarGroup>>
    private lateinit var seekBarAdapter: FastAdapter<FacePupTierWrapper.SeekBarItem>

    private val status = Status()

    init {
        binding = ViewControlTierFacepupBinding.inflate(LayoutInflater.from(context), this, true)
        initAdapter()
        initView()
    }

    private fun initView() {
        binding.groupRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
        binding.itemRecyclerView.apply {
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
            facePupTierWrapper?.reset()
            controlListener?.onResetClick()
        }
    }

    private fun initAdapter() {
        groupAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_group) { holder, bean, position ->
            val binding = ItemTierGroupBinding.bind(holder.itemView)
            binding.name.text = bean.name + bean.changeNum().let { if (it != 0) it else "" }
            if (status.groupIndex == position) {
                binding.root.setBackgroundColor(Color.parseColor("#094771"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            binding.root.setOnClickListener {
                eventListener.onGroupClick(bean, position)
            }
        }
        itemAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_group) { holder, bean, position ->
            val binding = ItemTierGroupBinding.bind(holder.itemView)
            binding.name.text = bean.name + bean.changeNum().let { if (it != 0) it else "" }
            if (status.itemIndex == position) {
                binding.root.setBackgroundColor(Color.parseColor("#ce834a"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            binding.root.setOnClickListener {
                eventListener.onItemClick(bean, position)
            }
        }
        itemTypeAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_group) { holder, bean, position ->
            val binding = ItemTierGroupBinding.bind(holder.itemView)
            binding.name.text = bean.first.showText() + bean.second.changeNum().let { if (it != 0) it else "" }
            if (status.itemTypeIndex == position) {
                binding.root.setBackgroundColor(Color.parseColor("#f88070"))
            } else {
                binding.root.setBackgroundColor(Color.parseColor("#FFFFFF"))
            }
            binding.root.setOnClickListener {
                eventListener.onItemTypeClick(bean, position)
            }
            binding.root.setOnLongClickListener {
                eventListener.onItemTypeLongClick(bean, position)
                true
            }
        }
        seekBarAdapter = FastAdapter(mutableListOf(), R.layout.item_tier_seek_bar) { holder, bean, position ->
            val binding = ItemTierSeekBarBinding.bind(holder.itemView)
            binding.name.text = bean.name
//            binding.seekBar.value = bean.value
//            binding.seekBar.clearOnChangeListeners()
//            binding.seekBar.addOnChangeListener { _, value, isUser ->
//                if (isUser) {
//                    eventListener.onSeekBarScroll(bean, value, position)
//                    bean.value = value
//                }
//            }
            binding.seekBar.apply {
                progress = (bean.value * 100).roundToInt()
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        eventListener.onSeekBarScroll(bean, progress/100f, position)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })
            }
        }
    }

    fun updateContainer(facePupTierWrapper: FacePupTierWrapper) {
        this.facePupTierWrapper = facePupTierWrapper
        groupAdapter.updateList(facePupTierWrapper.groupList)
        status.clickGroup(0)
        eventListener.onGroupClick(facePupTierWrapper.groupList[status.groupIndex], status.groupIndex)
    }

    fun setControlListener(listener: FacePupControlTierListener) {
        controlListener = listener
    }
}

/**
 * 事件监听，对子 View 的事件响应进行处理
 */
internal interface FacePupTierEventListener {

    fun onGroupClick(group: FacePupTierWrapper.Group, index: Int)

    fun onItemClick(item: FacePupTierWrapper.Item, index: Int)

    fun onItemTypeClick(itemType: Pair<FacePupTierWrapper.ControlMode, FacePupTierWrapper.SeekBarGroup>, index: Int)

    fun onItemTypeLongClick(itemType: Pair<FacePupTierWrapper.ControlMode, FacePupTierWrapper.SeekBarGroup>, index: Int)

    fun onSeekBarScroll(item: FacePupTierWrapper.SeekBarItem, scale: Float, index: Int)
}

/**
 * 封装给外界的接口
 */
interface FacePupControlTierListener {
    fun onSeekBarScroll(item: FacePupTierWrapper.SeekBarItem, scale: Float)

    fun onResetClick()
}