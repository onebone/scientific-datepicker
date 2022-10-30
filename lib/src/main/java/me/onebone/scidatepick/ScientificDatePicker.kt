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
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ScientificDatePicker(
	modifier: Modifier = Modifier
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

				detectDragGestures(
					onDragStart = {
						startEarthOffset = earthOffset!!
						accumulated = Offset.Zero
					},
					onDrag = { _, dragAmount ->
						accumulated += dragAmount

						val target = accumulated + startEarthOffset

						val radian = atan2(target.y - size.height / 2f, target.x - size.width / 2f)
						earthOffset = getOffsetByAngle(size, radian)
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
