package com.faceunity.fupta.facepup.weight

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.faceunity.app_ptag.databinding.ViewControlOriginFacepupBinding
import com.faceunity.fupta.facepup.entity.FacePupContainer
import com.faceunity.fupta.facepup.entity.FacePupGroup
import com.faceunity.fupta.facepup.entity.FacePupParam
import com.faceunity.fupta.facepup.weight.adapter.OriginGroupAdapter
import com.faceunity.fupta.facepup.weight.adapter.OriginSeekBarAdapter

/**
 *
 */
class FacePupOriginControlView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {

    private val binding: ViewControlOriginFacepupBinding
    private var eventListener: FacePupControlEventListener = object : FacePupControlEventListener {
        override fun onGroupItemClick(group: FacePupGroup) {
            seekBarAdapter.submitList(group.getAllFacePupParam())
        }

        override fun onSeekBarScroll(item: FacePupParam, scale: Float) {
            controlListener?.onSeekBarScroll(item, scale)
        }
    }
    private var controlListener: FacePupControlListener? = null

    private var facePupContainer: FacePupContainer? = null
    private val groupAdapter = OriginGroupAdapter(eventListener)
    private val seekBarAdapter = OriginSeekBarAdapter(eventListener)


    init {
        binding = ViewControlOriginFacepupBinding.inflate(LayoutInflater.from(context), this, true)
        initView()
    }

    private fun initView() {
        binding.groupRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = groupAdapter
        }
        binding.seekBarRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = seekBarAdapter
        }
    }

    fun updateContainer(newFacePupContainer: FacePupContainer) {
        facePupContainer = newFacePupContainer

        groupAdapter.submitList(newFacePupContainer.meshPoints.list)

    }

    fun setControlListener(facePupControlListener: FacePupControlListener) {
        controlListener = facePupControlListener
    }
}

/**
 * 事件监听，对子 View 的事件响应进行处理
 */
internal interface FacePupControlEventListener {

    fun onGroupItemClick(group: FacePupGroup)

    fun onSeekBarScroll(item: FacePupParam, scale: Float)
}

/**
 * 封装给外界的接口
 */
interface FacePupControlListener {
    fun onSeekBarScroll(item: FacePupParam, scale: Float)
}