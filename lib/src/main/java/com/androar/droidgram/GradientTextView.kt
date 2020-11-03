/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * This project is only for EDUCATIONAL PURPOSES and IS NOT related / DOES NOT tend
 * to copy any intellectual property of Instagram.
 *
 */

package com.androar.droidgram

import android.content.Context
import android.graphics.LinearGradient
import android.graphics.Shader
import android.util.AttributeSet
import androidx.core.content.ContextCompat

/**
 * [GradientTextView] is a sub-class of the [androidx.appcompat.widget.AppCompatTextView] view.
 * The function is to produce a TextView that has a gradient shader on top of it.
 *
 * It has two main parameters,
 * @param [colorOne] that defines the starting color on the tint of the TextView
 * which can be set through XML or [setGradientStartColor].
 *
 * @param [colorTwo] that defines the ending color on the tint of the TextView.
 * which can be set through XML or [setGradientEndColor].
 *
 * @author Bharadwaj Giridhar (goforbg.com)
 * @since 23-October-2020
 */
class GradientTextView : androidx.appcompat.widget.AppCompatTextView {

    private var colorOne: Int? = null
    private var colorTwo: Int? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttributes(attrs)
    }


    private fun initAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.GradientTextView, 0, 0
            )
            colorOne = a.getColor(R.styleable.GradientTextView_gradientStartColor,
                ContextCompat.getColor(context, R.color.default_gradient_text_view_color_one))
            colorTwo = a.getColor(R.styleable.GradientTextView_gradientEndColor,
                ContextCompat.getColor(context, R.color.default_gradient_text_view_color_two))
            a.recycle()
        }
    }

    fun setGradientStartColor(color: Int) {
        colorOne = color
        applyShade()
    }

    fun setGradientEndColor(color: Int) {
        colorTwo = color
        applyShade()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        applyShade()
    }

    private fun applyShade() {
        if (colorOne!=null && colorTwo!=null) {
            paint.shader = getShader(colorOne!!, colorTwo!!)
        } else {
            paint.shader = getShader(
                ContextCompat.getColor(context, R.color.default_gradient_text_view_color_one),
                ContextCompat.getColor(context, R.color.default_gradient_text_view_color_two)
            )
        }
    }

    private fun getShader(colorOne: Int, colorTwo: Int) : Shader? {
        return LinearGradient(
            0F, 0F, measuredWidth.toFloat(), textSize, intArrayOf(
                colorOne,
                colorTwo
            ), null, Shader.TileMode.CLAMP
        )
    }

}