package ru.example.core.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.example.core.R
import java.util.*
import kotlin.concurrent.schedule

class LoaderView @JvmOverloads constructor(
	context: Context,
	attrs: AttributeSet? = null,
	defStyleAttr: Int = 0
) : View(
	context,
	attrs,
	defStyleAttr
) {

	private var timer: Timer? = null

	private val radiusList = mutableListOf(
		LOADING_RADIUS,
		INITIAL_RADIUS,
		INITIAL_RADIUS,
		INITIAL_RADIUS,
		INITIAL_RADIUS
	)

	private var xPosition = 0
	private var yPosition = 0

	private val xSecondPosition by lazy(LazyThreadSafetyMode.NONE) { xPosition + POINTS_DISTANCE }
	private val xThirdPosition by lazy(LazyThreadSafetyMode.NONE) { xSecondPosition + POINTS_DISTANCE }
	private val xFourthPosition by lazy(LazyThreadSafetyMode.NONE) { xThirdPosition + POINTS_DISTANCE }
	private val xFifthPosition by lazy(LazyThreadSafetyMode.NONE) { xFourthPosition + POINTS_DISTANCE }

	private val paint = Paint()

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec)

		xPosition = ((measuredWidth - 280) / 2)
		yPosition = (measuredHeight / 2)
	}

	override fun onDraw(canvas: Canvas?) {
		super.onDraw(canvas)
		canvas?.drawCircle(
			xPosition.toFloat(),
			yPosition.toFloat(),
			radiusList[0].toFloat(),
			paint.apply {
				color = ContextCompat.getColor(context, R.color.loading_view_first_point)
			}
		)
		canvas?.drawCircle(
			xSecondPosition.toFloat(),
			yPosition.toFloat(),
			radiusList[1].toFloat(),
			paint.apply {
				color = ContextCompat.getColor(context, R.color.loading_view_second_point)
			})
		canvas?.drawCircle(
			xThirdPosition.toFloat(),
			yPosition.toFloat(),
			radiusList[2].toFloat(),
			paint.apply {
				color = ContextCompat.getColor(context, R.color.loading_view_third_point)
			}
		)
		canvas?.drawCircle(
			xFourthPosition.toFloat(),
			yPosition.toFloat(),
			radiusList[3].toFloat(),
			paint.apply {
				color = ContextCompat.getColor(context, R.color.loading_view_fourth_point)
			}
		)
		canvas?.drawCircle(
			xFifthPosition.toFloat(),
			yPosition.toFloat(),
			radiusList[4].toFloat(),
			paint.apply {
				color = ContextCompat.getColor(context, R.color.loading_view_fifth_point)
			}
		)
	}

	fun showLoading() {
		visibility = VISIBLE
		timer = Timer().also {
			it.schedule(delay = DELAY, period = DELAY, action = {
				for (i in 0 until radiusList.size) {
					if (radiusList[i] > INITIAL_RADIUS) {
						radiusList[i] = INITIAL_RADIUS
						if (i != radiusList.size - 1) {
							radiusList[i + 1] = LOADING_RADIUS
						} else {
							radiusList[0] = LOADING_RADIUS
						}
						break
					}
				}
				CoroutineScope(Dispatchers.Main).launch {
					invalidate()
				}
			})
		}
	}

	fun hideLoading() {
		visibility = GONE
		timer?.cancel()
	}

	companion object {
		private const val POINTS_DISTANCE = 60
		private const val INITIAL_RADIUS = 15
		private const val LOADING_RADIUS = 20
		private const val DELAY = 150L
	}
}