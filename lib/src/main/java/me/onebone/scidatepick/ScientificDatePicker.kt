package me.onebone.scidatepick

import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.awaitDragOrCancellation
import androidx.compose.foundation.gestures.awaitFirstDown
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
import androidx.compose.ui.input.pointer.consumePositionChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize

@Composable
fun ScientificDatePicker(
	modifier: Modifier
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

	Canvas(
		modifier = modifier
			.onGloballyPositioned {
				size = it.size
			}
			.pointerInput(Unit) {
				while (true) {
					awaitPointerEventScope {
						val pointer = awaitFirstDown()

						val change = awaitDragOrCancellation(pointer.id) ?: return@awaitPointerEventScope

						val position = change.position
						val delta = change.positionChange()
						change.consumePositionChange()
					}
				}
			}
	) {
		if (path != null) {
			drawPath(
				path = path,
				color = DefaultOrbitColor,
				style = Stroke(width = 5f)
			)
		}

		// TODO: remove it
		offsets.forEach {
			drawCircle(Color.Red, radius = 5f, center = it)
		}
	}
}

internal fun DrawScope.drawEarth(x: Float, y: Float) {
	drawCircle(Color.Blue, radius = 15f, center = Offset(x, y))
}
