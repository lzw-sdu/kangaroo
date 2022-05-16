package com.faceunity.fupta.facepup.weight.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.faceunity.app_ptag.databinding.ItemOriginSeekBarBinding
import com.faceunity.fupta.facepup.entity.FacePupParam
import com.faceunity.fupta.facepup.weight.FacePupControlEventListener

/**
 * Created on 2021/11/18 0018 15:15.


 */
internal class OriginSeekBarAdapter(
    private val controlEventListener: FacePupControlEventListener
    ) : ListAdapter<FacePupParam, OriginSeekBarAdapter.OriginSeekBarViewHolder>(
    FacePupParamDiffCallback
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OriginSeekBarViewHolder {
        return OriginSeekBarViewHolder(
            ItemOriginSeekBarBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            controlEventListener
        )
    }

    override fun onBindViewHolder(holder: OriginSeekBarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class OriginSeekBarViewHolder(
        private val binding: ItemOriginSeekBarBinding,
        private val controlEventListener: FacePupControlEventListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FacePupParam) {
            binding.name.text = item.key
            binding.seekBar.apply {
//                addOnChangeListener { _, value, _ ->
//                    controlEventListener.onSeekBarScroll(item, value)
//                }

                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        controlEventListener.onSeekBarScroll(item, progress/100f)
                    }

                    override fun onStartTrackingTouch(seekBar: SeekBar?) {

                    }

                    override fun onStopTrackingTouch(seekBar: SeekBar?) {

                    }
                })

            }
        }
    }

    object FacePupParamDiffCallback: DiffUtil.ItemCallback<FacePupParam>() {
        override fun areItemsTheSame(oldItem: FacePupParam, newItem: FacePupParam): Boolean {
            return oldItem.key == newItem.key
        }

        override fun areContentsTheSame(oldItem: FacePupParam, newItem: FacePupParam): Boolean {
            return oldItem.key == newItem.key
        }

    }


}