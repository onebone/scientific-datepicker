package me.onebone.scidatepick

import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

@Composable
fun ScientificDatePicker(
	modifier: Modifier,
	dragAmplifier: Float
) {
	val padding = with(LocalDensity.current) { DefaultDatePickerPadding.toPx() }

	var size by remember { mutableStateOf<IntSize?>(null) }

	val path = remember(size) {
		if (size == null) return@remember null

		Path().apply {
			addArc(
				oval = Rect(
					offset = Offset(padding, padding),
					size = Size(size!!.width - padding * 2, size!!.height - padding * 2)
				),
				startAngleDegrees = 0f,
				sweepAngleDegrees = 360f
			)
		}
	}

	val measure = remember(path) { PathMeasure(path?.asAndroidPath(), false) }
	val offsets = remember(measure) {
		val length = measure.length

		val fArr = FloatArray(2)

		List(300) {
			measure.getPosTan(length * it / 300f, fArr, null)
			Offset(fArr[0], fArr[1])
		}
	}

	var earthOffset by remember(offsets) { mutableStateOf(offsets[0]) }

	Canvas(
		modifier = modifier
			.onGloballyPositioned {
				size = it.size
			}
			.pointerInput(offsets) {
				var accumulated: Offset = Offset.Zero
				var startEarthOffset: Offset = Offset.Zero

				detectDragGestures(
					onDragStart = {
						startEarthOffset = earthOffset
						accumulated = Offset.Zero
					},
					onDrag = { _, dragAmount ->
						accumulated += dragAmount

						val target = accumulated + startEarthOffset

						earthOffset = offsets.minByOrNull {
							val dx = it.x - target.x
							val dy = it.y - target.y
							(dx * dx) + (dy * dy)
						}!!
					}
				)
			}
	) {
		if (path != null) {
			drawPath(
				path = path,
				color = DefaultOrbitColor,
				style = Stroke(width = 5f)
			)
		}

		drawCircle(
			color = Color.Blue,
			radius = 20f,
			center = earthOffset
		)
	}
}

internal fun DrawScope.drawEarth(x: Float, y: Float) {
	drawCircle(Color.Blue, radius = 15f, center = Offset(x, y))
}
