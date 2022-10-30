package me.onebone.scidatepick

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import java.time.LocalDate
import kotlin.math.*

const val PI_F = PI.toFloat()

@Composable
fun ScientificDatePicker(
	modifier: Modifier = Modifier,
	date: LocalDate,
	onDateChange: (LocalDate) -> Unit
) {
	var earthOffset by remember {
		mutableStateOf<Offset?>(null)
	}

	Canvas(
		modifier = modifier
			.onGloballyPositioned {
				if (earthOffset == null) {
					earthOffset = getOffsetByAngle(it.size, 0f)
				}
			}
			.pointerInput(Unit) {
				var accumulated: Offset = Offset.Zero
				var startEarthOffset: Offset = Offset.Zero
				var lastRadian = 0f

				detectDragGestures(
					onDragStart = {
						startEarthOffset = earthOffset!!
						accumulated = Offset.Zero
					},
					onDrag = { _, dragAmount ->
						accumulated += dragAmount

						val target = accumulated + startEarthOffset

						val radian = atan2(target.y - size.height / 2f, target.x - size.width / 2f).let {
							if (it < 0) it + PI_F*2
							else it
						}
						earthOffset = getOffsetByAngle(size, radian)

						val year =
							if (lastRadian in PI_F * 0.5f..PI_F && radian in PI_F..PI_F * 1.5f) {
								date.year + 1
							} else if (lastRadian in PI_F..PI_F * 1.5f && radian in PI_F * 0.5f..PI_F) {
								date.year - 1
							} else {
								date.year
							}

						lastRadian = radian

						val days = if (isLeapYear(year)) 366 else 365
						val progress = (radian + PI_F) / (2*PI_F) - 0.5f
						val nthDay = ceil(days * progress).toInt().coerceIn(1, days)
						onDateChange(LocalDate.ofYearDay(year, nthDay))
					}
				)
			}
	) {
		drawArc(DefaultOrbitColor, startAngle = 0f, sweepAngle = 360f, useCenter = false, style = Stroke(width = 5f))

		if (earthOffset != null) {
			drawEarth(earthOffset!!.x, earthOffset!!.y)
		}
	}
}

internal fun DrawScope.drawEarth(x: Float, y: Float) {
	drawCircle(Color.Blue, radius = 15f, center = Offset(x, y))
}

private fun getOffsetByAngle(size: IntSize, radian: Float): Offset {
	val horizontalRadius = size.width / 2f
	val verticalRadius = size.height / 2f

	return Offset(
		x = horizontalRadius * cos(radian) + horizontalRadius,
		y = verticalRadius * sin(radian) + verticalRadius
	)
}

private fun isLeapYear(year: Int): Boolean {
	return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)
}
