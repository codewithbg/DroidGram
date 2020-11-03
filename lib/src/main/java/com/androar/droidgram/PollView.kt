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
 * This project is only for EDUCATIONAL PURPOSES and IS NOT related / DOES NOT tend to copy any intellectual property of Instagram.
 *
 */

package com.androar.droidgram

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.Resources
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat

/**
 * [PollView] is a sub-class of [LinearLayout] class.
 * The core idea of this view is to implement a Poll feature with
 * two options.
 *
 * @param listener is used for getting a callback when [onOptionOneSelected] or [onOptionTwoSelected]
 * @see [attachCallBackListener] function to implement the callbacks.
 * This widget is pretty much useless without implementing this call-back.
 *
 * @param pollBackgroundColor defines the background color, which can be set from XML
 * or calling the function [setPollBackgroundColor]
 *
 * @param pollCardHeight defines the total height of the layout,
 * which can be set from XML or calling the function [setPollCardHeight]
 *
 * @param pollOptionsHeight defines the height of the options which is
 * again a horizontal linear layout with weights,
 * which can be set from XML or calling the function [setPollOptionsHeight]
 *
 * @param pollOptionsCornerRadius defines how curved the border radius for poll options,
 * which is again a horizontal linear layout.
 * @param dividerColor is the divider color between poll options,
 * it's hidden when an option is selected either using
 * [setOptionOnePercentage] or [setOptionTwoPercentage] is called.
 *
 * Are you actually reading this? wow I never thought I'd write something people would read.
 * @param questionText is used to set the question title's size from XML or setter [setPollQuestion].
 * @param questionTextSize is used to set the question title's size from XML or setter function [setQuestionTextSize].
 * @param questionTextColor is used to set the question title's size from XML or setter function [setQuestionTextColor].
 *
 * @param optionOneText is used to set the question title's size from XML or setter [setPollAnswers].
 * @param optionTwoText is used to set the question title's size from XML or setter [setPollAnswers].
 *
 * @param optionOneBackgroundColor can be changed from white to any color using xml or setter [setOptionOneBackgroundColor]
 * @param optionTwoBackgroundColor can be changed from white to any color using xml or setter [setOptionTwoBackgroundColor]
 *
 * OptionOneTextView and OptionTwoTextView are implementations of [GradientTextView]
 * they have parameters start and end color that define the tint of these colors.
 * @param optionOneStartColor can be changed from default to any using xml or setter [setOptionOneStartColor]
 * @param optionOneEndColor can be changed from default to any using xml or setter [setOptionOneEndColor]
 * @param optionTwoStartColor can be changed from default to any using xml or setter [setOptionTwoStartColor]
 * @param optionTwoEndColor can be changed from default to any using xml or setter [setOptionTwoEndColor]
 *
 * @param selectedOption can be set when [setOptionOnePercentage] or [setOptionTwoPercentage] is called
 *
 * @param optionOnePercentage or
 * @param optionTwoPercentage is set by corresponding setters in a float value from 0 to 100.
 *
 * @param defaultOptionOneFromValue is by default 50 in float and can be changed from xml
 * or [setDefaultOptionOneFromValue]
 * @param defaultOptionTwoFromValue is by default 50 in float and can be changed from xml
 * or [setDefaultOptionOneFromValue]
 * This is particularly useful if you're sending voted percentage to server and user kills
 * and logs onto the app.
 *
 * @param animationDuration is the animation duration in millis and again can be set using XML or
 * [setAnimationDuration]
 *
 * @param currentAnimationListener must be destroyed when detached from window to avoid memory leaks
 * since we use a [ValueAnimator] to perform our animation.
 * @see animate
 *
 * @param maxThreshold is used to limit the [setOptionOnePercentage] or [setOptionTwoPercentage]
 * so that the second option isn't knocked out of the layout completely.
 * @param minThreshold should be the complement of this value.
 * Change this using xml or setters.
 *
 * All getters are made public for better customization.
 *
 * @author Bharadwaj Giridhar (goforbg.com)
 * @since 23-October-2020
 */
class PollView : LinearLayout, View.OnClickListener {

    //CallBack listener
    private var listener: CardPollWidgetCallBack? = null

    //UI Elements
    //Background
    private var pollBackgroundColor: Int? = null
    private var pollCardHeight: Int? = null
    private var pollOptionsHeight: Int? = null
    private var pollOptionsCornerRadius: Int? = null
    private var dividerColor: Int? = null

    //QuestionText
    private var questionTextString: String? = null
    private var questionTextSize: Int? = null
    private var questionTextColor: Int? = null

    //OptionOne
    private var optionOneText: String? = null
    private var optionOneTextSize: Int? = null
    private var optionOneBackgroundColor: Int? = null
    private var optionOneStartColor: Int? = null
    private var optionOneEndColor: Int? = null

    //OptionTwo
    private var optionTwoText: String? = null
    private var optionTwoTextSize: Int? = null
    private var optionTwoBackgroundColor: Int? = null
    private var optionTwoStartColor: Int? = null
    private var optionTwoEndColor: Int? = null

    //SelectedOption
    private var selectedOption: Int? = null
    private var optionOnePercentage: Float? = null
    private var optionTwoPercentage: Float? = null

    //Animation Values
    private var defaultOptionOneFromValue = 50F
    private var defaultOptionTwoFromValue = 50F
    private var animationDuration: Int? = 500
    private var currentAnimatorListener: ValueAnimator? = null

    //Threshold Values
    private var maxThreshold: Float = Constants.DEFAULT_MAX_THRESHOLD_VALUE
    private var minThreshold: Float = Constants.DEFAULT_MIN_THRESHOLD_VALUE

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initAttributes(attrs)
        initLayout()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initAttributes(attrs)
        initLayout()
    }


    private fun initAttributes(attrs: AttributeSet?) {
        attrs?.let {
            val attributes = context.obtainStyledAttributes(
                attrs,
                R.styleable.PollView, 0, 0
            )

            questionTextString = attributes.getString(R.styleable.PollView_questionText)
            optionOneText = attributes.getString(R.styleable.PollView_optionOneText)
            optionTwoText = attributes.getString(R.styleable.PollView_optionTwoText)
            pollBackgroundColor = attributes.getColor(
                R.styleable.PollView_pollBackgroundColor,
                ContextCompat.getColor(
                    context,
                    R.color.default_poll_view_background
                )
            )
            pollCardHeight =
                attributes.getDimensionPixelSize(R.styleable.PollView_pollCardHeight, -420)
            pollOptionsHeight =
                attributes.getDimensionPixelSize(
                    R.styleable.PollView_pollOptionsHeight,
                    Constants.DEFAULT_POLL_OPTIONS_HEIGHT
                )
            pollOptionsCornerRadius =
                attributes.getDimensionPixelSize(
                    R.styleable.PollView_pollOptionsCornerRadius,
                    Constants.DEFAULT_CORNER_RADIUS
                )

            dividerColor = attributes.getColor(
                R.styleable.PollView_pollDividerColor, ContextCompat.getColor(
                    context,
                    R.color.default_poll_view_divider_color
                )
            )

            questionTextSize = attributes.getDimensionPixelSize(
                R.styleable.PollView_questionTextSize,
                Constants.DEFAULT_QUESTION_TEXT_SIZE
            )

            questionTextColor = attributes.getColor(
                R.styleable.PollView_questionTextColor, ContextCompat.getColor(
                    context,
                    R.color.default_poll_view_question_color
                )
            )

            optionOneTextSize =
                attributes.getDimensionPixelSize(
                    R.styleable.PollView_optionOneTextSize,
                    Constants.DEFAULT_OPTION_ONE_TEXT_SIZE
                )

            optionOneBackgroundColor = attributes.getColor(
                R.styleable.PollView_questionTextColor, ContextCompat.getColor(
                    context,
                    R.color.default_poll_view_option_one_background
                )
            )

            optionOneStartColor = attributes.getColor(
                R.styleable.PollView_optionOneStartColor, ContextCompat.getColor(
                    context,
                    R.color.default_gradient_text_view_color_one
                )
            )
            optionOneEndColor = attributes.getColor(
                R.styleable.PollView_optionOneEndColor, ContextCompat.getColor(
                    context,
                    R.color.default_gradient_text_view_color_two
                )
            )

            optionTwoTextSize =
                attributes.getDimensionPixelSize(
                    R.styleable.PollView_optionTwoTextSize,
                    Constants.DEFAULT_OPTION_TWO_TEXT_SIZE
                )
            optionTwoBackgroundColor = attributes.getColor(
                R.styleable.PollView_questionTextColor, ContextCompat.getColor(
                    context,
                    R.color.default_poll_view_option_two_background
                )
            )
            optionTwoStartColor = attributes.getColor(
                R.styleable.PollView_optionTwoStartColor, ContextCompat.getColor(
                    context,
                    R.color.default_gradient_text_view_color_three
                )
            )
            optionTwoEndColor = attributes.getColor(
                R.styleable.PollView_optionTwoEndColor, ContextCompat.getColor(
                    context,
                    R.color.default_gradient_text_view_color_four
                )
            )

            defaultOptionOneFromValue =
                attributes.getFloat(
                    R.styleable.PollView_defaultOptionOneFromValue,
                    Constants.DEFAULT_OPTION_ONE_FROM_VALUE
                )
            defaultOptionTwoFromValue =
                attributes.getFloat(
                    R.styleable.PollView_defaultOptionTwoFromValue,
                    Constants.DEFAULT_OPTION_TWO_FROM_VALUE
                )

            animationDuration = attributes.getInt(
                R.styleable.PollView_pollAnimationDuration,
                Constants.DEFAULT_ANIMATION_DURATION
            )

            minThreshold = attributes.getFloat(
                R.styleable.PollView_minThresholdValue,
                Constants.DEFAULT_MIN_THRESHOLD_VALUE
            )

            maxThreshold = attributes.getFloat(
                R.styleable.PollView_minThresholdValue,
                Constants.DEFAULT_MIN_THRESHOLD_VALUE
            )

            attributes.recycle()
        }
    }


    private fun initLayout() {
        View.inflate(context, R.layout.poll_view_layout, this)

        questionTextString?.let {
            setPollQuestion(it)
        }

        setPollAnswers(optionOneText, optionTwoText)

        pollBackgroundColor?.let {
            setPollBackgroundColor(it)
        }

        pollCardHeight?.let {
            setPollCardHeight(it)
        }

        pollOptionsHeight?.let {
            setPollOptionsHeight(it)
        }

        pollOptionsCornerRadius?.let {
            setPollOptionsCornerRadius(it)
        }

        dividerColor?.let {
            setDividerColor(it)
        }

        questionTextSize?.let {
            setQuestionTextSize(it)
        }

        questionTextColor?.let {
            setQuestionTextColor(it)
        }

        optionOneTextSize?.let {
            setOptionOneTextSize(it)
        }

        optionOneBackgroundColor?.let {
            setOptionOneBackgroundColor(it)
        }

        optionOneStartColor?.let {
            setOptionOneStartColor(it)
        }

        optionOneEndColor?.let {
            setOptionOneEndColor(it)
        }

        optionTwoTextSize?.let {
            setOptionTwoTextSize(it)
        }

        optionTwoBackgroundColor?.let {
            setOptionTwoBackgroundColor(it)
        }

        optionTwoStartColor?.let {
            setOptionTwoStartColor(it)
        }

        optionTwoEndColor?.let {
            setOptionTwoEndColor(it)
        }

        defaultOptionOneFromValue?.let {
            setDefaultOptionOneFromValue(it)
        }

        defaultOptionTwoFromValue?.let {
            setDefaultOptionTwoFromValue(it)
        }

        animationDuration?.let {
            setAnimationDuration(it)
        }

        setMinThreshold(minThreshold)

        setMaxThreshold(maxThreshold)


    }

    private fun setAnimationDuration(duration: Int) {
        animationDuration = duration
    }

    private fun setDefaultOptionTwoFromValue(value: Float) {
        defaultOptionTwoFromValue = value
    }

    private fun setDefaultOptionOneFromValue(value: Float) {
        defaultOptionOneFromValue = value
    }

    fun setOptionOneStartColor(color: Int) {
        this.optionOneStartColor = color
        getPollOptionOneView()?.setGradientStartColor(color)
    }

    fun setOptionOneEndColor(color: Int) {
        this.optionOneEndColor = color
        getPollOptionOneView()?.setGradientEndColor(color)
    }

    fun setOptionTwoStartColor(color: Int) {
        optionTwoStartColor = color
        getPollOptionTwoView()?.setGradientStartColor(color)
    }

    fun setOptionTwoEndColor(color: Int) {
        this.optionTwoEndColor = color
        getPollOptionTwoView()?.setGradientEndColor(color)
    }

    fun setOptionOneBackgroundColor(pollOptionOneBackgroundColor: Int) {
        this.optionOneBackgroundColor = pollOptionOneBackgroundColor
        getPollOptionOneView()?.setBackgroundColor(pollOptionOneBackgroundColor)
    }

    fun setOptionTwoBackgroundColor(pollOptionTwoBackgroundColor: Int) {
        this.optionTwoBackgroundColor = pollOptionTwoBackgroundColor
        getPollOptionTwoView()?.setBackgroundColor(pollOptionTwoBackgroundColor)
    }

    fun setOptionOneTextSize(optionOneTextSize: Int) {
        this.optionOneTextSize = optionOneTextSize
        getPollOptionOneView()?.textSize = px2dp(resources, optionOneTextSize.toFloat())
    }

    fun setOptionTwoTextSize(optionTwoTextSize: Int) {
        this.optionTwoTextSize = optionTwoTextSize
        getPollOptionTwoView()?.textSize = px2dp(resources, optionTwoTextSize.toFloat())
    }

    fun setQuestionTextColor(questionTextColor: Int) {
        this.questionTextColor = questionTextColor
        getPollQuestionTextView()?.setTextColor(questionTextColor)
    }

    fun setQuestionTextSize(questionTextSize: Int) {
        this.questionTextSize = questionTextSize
        getPollQuestionTextView()?.textSize = px2dp(resources, questionTextSize.toFloat())
    }

    fun setDividerColor(dividerColor: Int) {
        if (dividerColor != -1) {
            this.dividerColor = dividerColor
            getDividerView()?.setBackgroundColor(dividerColor)
        }
    }

    fun setPollOptionsCornerRadius(cornerRadius: Int) {
        if (cornerRadius != -1) {
            this.pollOptionsCornerRadius = cornerRadius
            getPollOptionsContainer()?.radius = px2dp(resources, cornerRadius.toFloat())
        }
    }

    fun setPollCardHeight(pollCardHeight: Int) {
        if (pollCardHeight != -420) {
            this.pollCardHeight = dp2px(resources, pollCardHeight)
            val params: ViewGroup.LayoutParams? = getPollBackgroundView()?.layoutParams
            params?.width = ViewGroup.LayoutParams.MATCH_PARENT
            params?.height = dp2px(resources, pollCardHeight)
            getPollBackgroundView()?.layoutParams = params
        }
    }

    fun setPollOptionsHeight(pollOptionsHeight: Int) {
        this.pollOptionsHeight = dp2px(resources, pollOptionsHeight)
        val params = getPollOptionsContainer()?.layoutParams
        params?.width = -1
        params?.height = dp2px(resources, pollOptionsHeight)
        getPollOptionsContainer()?.layoutParams = params
    }


    fun attachCallBackListener(listener: CardPollWidgetCallBack) {
        this.listener = listener
    }

    /**
     * This can be an implementation challenge since some use Glide,
     * some use Fresco, Picasso, Coil etc.
     * Instead
     * @see getPollBackgroundView
     * and apply your image there as shown in the example.
     */
    fun attachPollImage(imageUrl: String) {

    }


    fun getPollOptionsContainer(): CardView? {
        return rootView?.rootView?.findViewById(R.id.poll_options_container) as CardView?
    }

    /**
     * @return [LinearLayout] view to perform image loading,
     * set alpha values,
     * or even layout params.
     */
    fun getPollBackgroundView(): LinearLayout? {
        return rootView?.rootView as LinearLayout?
    }

    fun getPollQuestionTextView(): AppCompatTextView? {
        return rootView?.rootView?.findViewById(R.id.poll_view_question_text_view) as AppCompatTextView?
    }

    fun getPollOptionOneView(): GradientTextView? {
        return rootView?.rootView?.findViewById(R.id.poll_view_option_1) as GradientTextView?
    }

    fun getPollOptionTwoView(): GradientTextView? {
        return rootView?.rootView?.findViewById(R.id.poll_view_option_2) as GradientTextView?
    }

    fun getDividerView(): View? {
        return rootView?.rootView?.findViewById(R.id.poll_view_divider) as View?
    }


    fun setPollBackgroundColor(color: Int?) {
        if (color != null) {
            this.pollBackgroundColor = color
            getPollBackgroundView()?.setBackgroundColor(color)
        }
    }

    fun setPollQuestion(questionString: String) {
        this.questionTextString = questionString
        (rootView?.rootView?.findViewById(R.id.poll_view_question_text_view) as AppCompatTextView?)?.text =
            questionString
    }

    fun setPollAnswers(optionOne: String?, optionTwo: String?) {
        this.optionOneText = optionOne
        this.optionTwoText = optionTwo
        val optionOneView =
            rootView?.rootView?.findViewById(R.id.poll_view_option_1) as AppCompatTextView?
        val optionTwoView =
            rootView?.rootView?.findViewById(R.id.poll_view_option_2) as AppCompatTextView?
        optionOne?.let {
            optionOneView?.text = it
            optionOneView?.setOnClickListener(this)
        }
        optionTwo?.let {
            optionTwoView?.text = it
            optionTwoView?.setOnClickListener(this)
        }
    }

    fun showDivider() {
        getDividerView()?.visibility = View.VISIBLE
    }

    fun hideDivider() {
        getDividerView()?.visibility = View.GONE
    }

    fun setMinThreshold(value: Float?) {
        value?.let {
            this.minThreshold = value
        }
    }

    fun setMaxThreshold(value: Float?) {
        value?.let {
            this.maxThreshold = value
        }
    }

    fun setOptionOnePercentage(value: Float) {
        hideDivider()
        selectedOption = 1
        this.optionOnePercentage = value
        if (value < maxThreshold || value == Constants.MAX_POLL_OPTIONS_VALUE) {
            val actualValue = Constants.MAX_POLL_OPTIONS_VALUE - value
            animate(getPollOptionOneView(), defaultOptionOneFromValue, actualValue)
            animate(getPollOptionTwoView(), defaultOptionTwoFromValue, value)
        } else {
            val actualValue = Constants.MAX_POLL_OPTIONS_VALUE - value + minThreshold
            animate(getPollOptionOneView(), defaultOptionOneFromValue, actualValue)
            animate(getPollOptionTwoView(), defaultOptionTwoFromValue, value - minThreshold)
        }
        getPollOptionOneView()?.text =
            String.format(
                context.getString(R.string.option_result),
                optionOneText,
                value.toInt().toString()
            )

        getPollOptionTwoView()?.text =
            String.format(
                context.getString(R.string.option_result),
                optionTwoText,
                (Constants.MAX_POLL_OPTIONS_VALUE - value).toInt().toString()
            )
        getPollOptionOneView()?.setBackgroundResource(R.drawable.poll_view_chosen_background)
    }

    fun setOptionTwoPercentage(value: Float) {
        hideDivider()
        selectedOption = 2
        this.optionTwoPercentage = value
        if (value < maxThreshold || value == Constants.MAX_POLL_OPTIONS_VALUE) {
            val actualValue = Constants.MAX_POLL_OPTIONS_VALUE - value
            animate(getPollOptionTwoView(), defaultOptionTwoFromValue, actualValue)
            animate(getPollOptionOneView(), defaultOptionOneFromValue, value)
        } else {
            val actualValue = Constants.MAX_POLL_OPTIONS_VALUE - value + minThreshold
            animate(getPollOptionTwoView(), defaultOptionTwoFromValue, actualValue)
            animate(getPollOptionOneView(), defaultOptionOneFromValue, value - minThreshold)
        }
        getPollOptionOneView()?.text =
            String.format(
                context.getString(R.string.option_result),
                optionOneText,
                (Constants.MAX_POLL_OPTIONS_VALUE - value).toInt().toString()
            )

        getPollOptionTwoView()?.text =
            String.format(
                context.getString(R.string.option_result),
                optionTwoText,
                value.toInt().toString()
            )
        getPollOptionTwoView()?.setBackgroundResource(R.drawable.poll_view_chosen_background)
    }

    fun reset() {
        this.optionOnePercentage = defaultOptionOneFromValue
        this.optionTwoPercentage = defaultOptionOneFromValue
        setOptionOnePercentage(defaultOptionOneFromValue)
        setOptionTwoPercentage(defaultOptionOneFromValue)
        showDivider()
        getPollOptionOneView()?.text = optionOneText
        getPollOptionTwoView()?.text = optionTwoText
        getPollOptionOneView()?.setBackgroundResource(R.drawable.poll_view_default_background)
        getPollOptionTwoView()?.setBackgroundResource(R.drawable.poll_view_default_background)
    }

    /**
     * Gets a parameter
     * @param v which is the view that has to be animated
     * @param from which is the start value of animation and
     * @param to which is the end value when animation is complete.
     */
    fun animate(v: View?, from: Float, to: Float) {
        currentAnimatorListener = ValueAnimator.ofFloat(from, to)
        currentAnimatorListener?.duration = animationDuration?.toLong() ?: 500L
        currentAnimatorListener?.addUpdateListener { animation ->
            val growingWeight = animation.animatedValue as Float
            val params = v?.layoutParams as LinearLayout.LayoutParams
            params.weight = growingWeight
            v.layoutParams = params
        }
        currentAnimatorListener?.start()
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.poll_view_option_1 -> {
                optionOneText?.let { listener?.onOptionOneSelected(it) }
            }
            R.id.poll_view_option_2 -> {
                optionTwoText?.let { listener?.onOptionTwoSelected(it) }
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        currentAnimatorListener?.removeAllUpdateListeners()
    }

    interface CardPollWidgetCallBack {
        fun onOptionOneSelected(pollAnswer: String?)
        fun onOptionTwoSelected(pollAnswer: String?)
    }

    /**
     * @param resource contains display metrics to convert the incoming value in
     * @param dp to
     * @return a float value that's converted to px value.
     */
    private fun dp2px(resource: Resources, dp: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp.toFloat(), resource.displayMetrics
        ).toInt()
    }

    /**
     * @param resource contains display metrics to convert the incoming value in
     * @param px to
     * @return a float value that's converted to dp value.
     */
    private fun px2dp(resource: Resources, px: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PX,
            px,
            resource.displayMetrics
        )
    }


}