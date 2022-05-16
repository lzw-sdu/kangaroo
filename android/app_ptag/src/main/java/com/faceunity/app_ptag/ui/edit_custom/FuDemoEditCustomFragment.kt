package com.faceunity.app_ptag.ui.edit_custom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.faceunity.app_ptag.FuDevInitializeWrapper
import com.faceunity.app_ptag.R
import com.faceunity.app_ptag.databinding.FuDemoEditCustomFragmentBinding
import com.faceunity.app_ptag.ui.edit_custom.entity.CustomControlModel
import com.faceunity.app_ptag.util.ToastUtils
import com.faceunity.core.camera.entity.FUCameraConfig
import com.faceunity.core.entity.FURenderInputData
import com.faceunity.core.entity.FURenderOutputData
import com.faceunity.core.renderer.entity.FUDrawFrameMatrix
import com.faceunity.core.renderer.impl.FUCustomRenderer
import com.faceunity.core.renderer.infe.OnGLRendererListener
import com.faceunity.editor_ptag.util.visibleOrGone
import com.faceunity.app_ptag.util.FastAdapter
import com.faceunity.toolbox.media.FUMediaUtils

class FuDemoEditCustomFragment : Fragment() {

    companion object {
        fun newInstance() = FuDemoEditCustomFragment()
    }

    private lateinit var binding: FuDemoEditCustomFragmentBinding

    private lateinit var viewModel: FuDemoEditCustomViewModel

    private val ptaRenderer = FUCustomRenderer()

    private lateinit var masterAdapter: FastAdapter<CustomControlModel.MainMenu>
    private lateinit var minorAdapter: FastAdapter<CustomControlModel.MinorMenu>
    private lateinit var itemAdapter: FastAdapter<CustomControlModel.Item>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FuDemoEditCustomFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FuDemoEditCustomViewModel::class.java)

        initView()
        initRenderer()
        inject()

        viewModel.requestData()
    }

    private fun initView() {
        binding.mainRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            masterAdapter = FastAdapter(mutableListOf(), R.layout.fu_demo_item_edit_custom) { holder, bean, position ->
                holder.itemView.findViewById<ImageView>(R.id.icon).let {
                    it.setImageBitmap(FUMediaUtils.loadBitmap(requireContext(), bean.icon ?: ""))
                }
                holder.itemView.setOnClickListener {
                    minorAdapter.updateList(bean.list)
                    minorAdapter.notifyDataSetChanged()
                }
            }
            adapter = masterAdapter
        }
        binding.sedRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            minorAdapter = FastAdapter(mutableListOf(), R.layout.fu_demo_item_edit_custom) { holder, bean, position ->
                holder.itemView.findViewById<ImageView>(R.id.icon).let {
                    it.setImageBitmap(FUMediaUtils.loadBitmap(requireContext(), bean.icon ?: ""))
                }
                holder.itemView.setOnClickListener {
                    itemAdapter.updateList(bean.list)
                    itemAdapter.notifyDataSetChanged()
                }
            }
            adapter = minorAdapter
        }
        binding.thirdRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            itemAdapter = FastAdapter(mutableListOf(), R.layout.fu_demo_item_edit_custom) { holder, bean, position ->
                holder.itemView.findViewById<ImageView>(R.id.icon).let {
                    it.setImageBitmap(FUMediaUtils.loadBitmap(requireContext(), bean.icon ?: ""))
                }
                holder.itemView.findViewById<TextView>(R.id.lock_icon).let {
                    it.visibleOrGone(bean.isLock)
                }
                holder.itemView.findViewById<TextView>(R.id.new_icon).let {
                    it.visibleOrGone(bean.isNew)
                }
                holder.itemView.setOnClickListener {
                    if (bean.isLock) {
                        ToastUtils.showFailureToast(requireContext(), "还未解锁该道具")

                    } else {
                        viewModel.clickItem(bean)
                    }
                }
            }
            adapter = itemAdapter
        }
    }

    private fun initRenderer() {
        ptaRenderer.apply {
            bindCameraConfig(FUCameraConfig())
            setDefaultRenderType(FUCustomRenderer.RendererTypeEnum.EMPTY_TEXTURE)
            bindGLTextureView(binding.glTextureView)
            bindListener(object : OnGLRendererListener {
                override fun onSurfaceCreated() {
                    FuDevInitializeWrapper.initRenderKit()
                    viewModel.loadDefaultAvatar()
                }

                override fun onSurfaceChanged(width: Int, height: Int) {
                    ptaRenderer.setEmptyTextureConfig(width, height) //渲染依据实际分辨率设置
                }

                override fun onRenderBefore(inputData: FURenderInputData) {

                }

                override fun onRenderAfter(
                    outputData: FURenderOutputData,
                    drawMatrix: FUDrawFrameMatrix
                ) {
                }

                override fun onDrawFrameAfter() {

                }

                override fun onSurfaceDestroy() {
                    FuDevInitializeWrapper.releaseRenderKit()
                }
            })
        }
    }

    private fun inject() {
        viewModel.controlCustomModelLiveData.observe(viewLifecycleOwner) {
            masterAdapter.updateList(it.list)
            masterAdapter.notifyDataSetChanged()
        }
    }

    override fun onResume() {
        super.onResume()
        ptaRenderer.resumeRender()
    }

    override fun onPause() {
        super.onPause()
        ptaRenderer.pauseRender()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        ptaRenderer.release()
        FuDevInitializeWrapper.releaseRenderKit()
    }

}