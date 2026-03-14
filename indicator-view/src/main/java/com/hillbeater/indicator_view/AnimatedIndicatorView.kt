package com.hillbeater.indicator_view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2

class AnimatedIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val leftDots = LinearLayout(context)
    private val rightDots = LinearLayout(context)
    private val counter = TextView(context)

    private var totalPages = 0
    private var currentPage = 0

    private val maxVisibleDots = 3

    init {

        orientation = HORIZONTAL
        gravity = Gravity.CENTER

        leftDots.orientation = HORIZONTAL
        rightDots.orientation = HORIZONTAL

        counter.setPadding(dpToPx(12), dpToPx(4), dpToPx(12), dpToPx(4))
        counter.setBackgroundResource(R.drawable.bg_counter)
        counter.setTextColor(
            ContextCompat.getColor(context, R.color.indicator_counter_text)
        )
        counter.textSize = 12f

        addView(leftDots)
        addView(counter)
        addView(rightDots)
    }

    fun setTotalPages(total: Int) {
        totalPages = total
        update()
    }

    fun setCurrentPage(position: Int) {
        currentPage = position
        update()
    }

    private fun update() {

        if (totalPages == 0) return

        counter.text = "${currentPage + 1}/$totalPages"

        animateCounter()

        leftDots.removeAllViews()
        rightDots.removeAllViews()

        val start = maxOf(0, currentPage - maxVisibleDots)
        val end = minOf(totalPages, currentPage + maxVisibleDots + 1)

        // LEFT DOTS
        for (i in start until currentPage) {

            val isNearCounter = i == currentPage - 1

            leftDots.addView(createDot(isNearCounter))
        }

        // RIGHT DOTS
        for (i in currentPage + 1 until end) {

            val isNearCounter = i == currentPage + 1

            rightDots.addView(createDot(isNearCounter))
        }
    }

    private fun createDot(isNearCounter: Boolean): View {

        val dot = View(context)

        val size = dpToPx(6)
        val params = LayoutParams(size, size)
        params.marginEnd = dpToPx(4)

        dot.layoutParams = params

        dot.setBackgroundResource(
            if (isNearCounter)
                R.drawable.dot_dark
            else
                R.drawable.dot
        )

        dot.alpha = 0f
        dot.scaleX = 0.5f
        dot.scaleY = 0.5f

        dot.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(200)
            .start()

        return dot
    }

    private fun animateCounter() {

        counter.scaleX = 0.8f
        counter.scaleY = 0.8f

        counter.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(150)
            .start()
    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    fun attachToViewPager(viewPager: ViewPager2) {

        setTotalPages(viewPager.adapter?.itemCount ?: 0)

        viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {

                override fun onPageSelected(position: Int) {
                    setCurrentPage(position)
                }
            }
        )
    }
}