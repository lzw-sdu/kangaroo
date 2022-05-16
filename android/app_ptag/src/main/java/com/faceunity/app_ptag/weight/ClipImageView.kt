package com.faceunity.app_ptag.weight

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.use
import com.faceunity.app_ptag.R

/**
 * Created on 2021/7/23 0023 19:14.


 * 可对图片进行圆角矩形、圆形显示的 ImageView
 */
class ClipImageView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private val paint = Paint().apply {
        isAntiAlias = true
    }
    private val clipPath = Path()
    var type = Type.Circle
    var radius = getContext().resources.getDimensionPixelSize(R.dimen.item_round_rect_radius).toFloat()

    init {
        attrs?.let {
            context.obtainStyledAttributes(it, R.styleable.ClipImageView)
        }?.use {
            val typeString = it.getString(R.styleable.ClipImageView_clip_type)?.lowercase()
            when(typeString) {
                "circle" -> type = Type.Circle
                "roundrect" -> type = Type.RoundRect
                "none" -> type = Type.None
            }
            val radiusValue = it.getFloat(R.styleable.ClipImageView_round_radius, radius)
            radius = radiusValue
        }

    }

    override fun onDraw(canvas: Canvas) {
        clipPath.reset()
        val width = width
        val height = height
        val min = Math.min(width, height)
        when(type) {
            Type.Circle -> {
                clipPath.addCircle(width / 2f, height / 2f, min / 2f, Path.Direction.CW)
            }
            Type.RoundRect -> {
                clipPath.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), radius, radius, Path.Direction.CW)
            }
            Type.None -> {}
        }
        canvas.clipPath(clipPath)
        super.onDraw(canvas)
    }

    enum class Type {
        Circle, RoundRect, None
    }
}