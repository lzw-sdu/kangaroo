package com.faceunity.app_ptag.weight.avatar_manager

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.faceunity.app_ptag.databinding.FuRecyclerViewBottomsheetBinding
import com.faceunity.app_ptag.weight.avatar_manager.adapter.AvatarManagerAdapter
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarContainer
import com.faceunity.app_ptag.weight.avatar_manager.entity.FuAvatarWrapper
import com.faceunity.editor_ptag.util.invisible
import com.faceunity.editor_ptag.util.visible
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*

/**
 * Created on 2021/12/13 0013 19:40.
 */
class AvatarManagerDialog(context: Context, val onItemClick: (item: FuAvatarWrapper) -> Unit, val onItemDelete: (item: FuAvatarWrapper) -> Unit) : BottomSheetDialog(context) {
    private lateinit var binding: FuRecyclerViewBottomsheetBinding
    private val avatarManagerAdapter: AvatarManagerAdapter = AvatarManagerAdapter(object : AvatarManagerAdapter.Listener {
        override fun onClick(item: FuAvatarWrapper) {
            onItemClick(item)
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FuRecyclerViewBottomsheetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.title.text = "形象管理"
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(context, 5)
            adapter = avatarManagerAdapter
        }
        stickerTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    fun syncAvatarContainer(fuAvatarContainer: FuAvatarContainer) {
        avatarManagerAdapter.selectId = fuAvatarContainer.selectId
        avatarManagerAdapter.submitList(fuAvatarContainer.avatarList)
        customStickerList.apply {
            clear()
            addAll(fuAvatarContainer.avatarList)
        }
        //TODO 需要根据 selectID 刷新指定的 Item
        avatarManagerAdapter.notifyDataSetChanged()
    }


    //region 拖拽功能

    var customStickerList: MutableList<FuAvatarWrapper> = mutableListOf()

    private val stickerTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        private var isTouchUp = false

        private var dragListener: DragListener = object : DragListener {
            override fun deleteState(delete: Boolean) {
                (binding.deleteLayout).let {
                    if (delete) {
                        it.setBackgroundColor(Color.parseColor("#CF6034"))
                    } else {
                        it.setBackgroundColor(Color.parseColor("#F68149"))
                    }

                }
            }

            override fun dragState(start: Boolean) {
                if (start) {
                    binding.deleteLayout.visible()
//                    VibratorUtil.vibrate(FUARVideoApp.sContext)
                } else {
                    binding.deleteLayout.invisible()
//                    saveCustomList()
                }
            }

            override fun remove(position: Int) {
                onItemDelete(customStickerList[position])
                customStickerList.removeAt(position)
                //使用 notifyItemRemoved 时会造成数据样式错乱，因为不会重新 bind holder。按理论来说加后面的 notifyItemRangeRemoved 就可以了，但是依然有问题，不管了=。=
//                avatarManagerAdapter.notifyItemRemoved(position)
//                avatarManagerAdapter.notifyItemRangeRemoved(position, avatarManagerAdapter.itemCount - position)
                avatarManagerAdapter.notifyDataSetChanged()
            }
        }


        override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
            var dragFlag = 0
            if (recyclerView.layoutManager is GridLayoutManager) {
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            } else if (recyclerView.layoutManager is LinearLayoutManager) {
                dragFlag = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            }
            val swipeFlag = 0
            return makeMovementFlags(dragFlag, swipeFlag)
        }

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            //得到当拖拽的viewHolder的Position
            val fromPosition = viewHolder.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            if (toPosition == 0 || toPosition == customStickerList.size) {
                return false
            }
            if (fromPosition < toPosition) {
                for (i in fromPosition until toPosition) {
                    Collections.swap(customStickerList, i, i + 1)
                }
            } else {
                for (i in fromPosition downTo toPosition + 1) {
                    Collections.swap(customStickerList, i, i - 1)
                }
            }
            avatarManagerAdapter.notifyItemMoved(fromPosition, toPosition)
            return true
        }

        override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
//            FULog.d("dx:$dX, dy:$dY, 列表本身高度：${recyclerView.height}, 底部：${viewHolder.itemView.bottom}")
//            FULog.d("当前操作：${viewHolder.adapterPosition}, 是否抬起手指：$isTouchUp")
            val removeBtnXY = IntArray(2)
            binding.deleteLayout.getLocationOnScreen(removeBtnXY)
            val itemXY = IntArray(2)
            viewHolder.itemView.getLocationOnScreen(itemXY)
            if (Math.abs(itemXY[1] - removeBtnXY[1]) <= 100) {
                dragListener.deleteState(true)
                if (isTouchUp) {
//                    viewHolder.itemView.visibility = View.INVISIBLE
                    if (viewHolder.adapterPosition != -1) {
                        dragListener.remove(viewHolder.adapterPosition)
                        reset()
                        return
                    }
                }
            } else {
                dragListener.deleteState(false)
            }
            //如果不加这行，那么 onChildDraw 会一直绘制直到经过删除触发区。此处代码可以确保删除逻辑只在刚触发抬手时间时执行。
            isTouchUp = false

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            customStickerList.removeAt(direction)
            avatarManagerAdapter.notifyItemRemoved(direction)
        }

        override fun isLongPressDragEnabled(): Boolean {
            return true
        }

        override fun getAnimationDuration(recyclerView: RecyclerView, animationType: Int, animateDx: Float, animateDy: Float): Long {
            isTouchUp = true
            return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy)
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                dragListener.dragState(true)
            }
            super.onSelectedChanged(viewHolder, actionState)
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            reset()
        }

        private fun reset() {
            dragListener.dragState(false)
            isTouchUp = false
        }

    })

    /**
     * 拖拽删除监听
     */
    interface DragListener {
        /**
         * 用户是否将 item拖动到删除处，根据状态改变颜色
         */
        fun deleteState(delete: Boolean)

        /**
         * 是否于拖拽状态
         */
        fun dragState(start: Boolean)

        /**
         * 当用户与item的交互结束并且item也完成了动画时调用
         */
        fun remove(position: Int)
    }

    //endregion

}