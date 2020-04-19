package com.example.loftmoney

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class DiagramView : View {
    private var mExpences = 0f
    private var mIncome = 0f
    private val expencePaint = Paint()
    private val incomePaint = Paint()

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    fun update(expences: Float, income: Float) {
        mExpences = expences
        mIncome = income
        invalidate()
    }

    private fun init() {
        expencePaint.color = ContextCompat.getColor(context, R.color.dark_sky_blue)
        incomePaint.color = ContextCompat.getColor(context, R.color.apple_green)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val total = mExpences + mIncome
        val expenceAngle = 360f * mExpences / total
        val incomeAngle = 360f * mIncome / total
        val space = 10
        val size = Math.min(width, height) - space * 2
        val xMargin = (width - size) / 2
        val yMargin = (height - size) / 2
        canvas.drawArc(
            xMargin - space.toFloat(), yMargin.toFloat(), width - xMargin - space.toFloat(),
            height - yMargin.toFloat(), 180 - expenceAngle / 2, expenceAngle, true, expencePaint
        )
        canvas.drawArc(
            xMargin + space.toFloat(), yMargin.toFloat(), width - xMargin + space.toFloat(),
            height - yMargin.toFloat(), 360 - incomeAngle / 2, incomeAngle, true, incomePaint
        )
    }
}