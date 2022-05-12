package me.onebone.scidatepick

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity

@Composable
fun ScientificDatePicker(
	modifier: Modifier
) {
	val padding = with(LocalDensity.current) { DefaultDatePickerPadding.toPx() }

	Canvas(modifier = modifier) {
		val size = size

		val path = Path().apply {
			addArc(
				oval = Rect(
					offset = Offset(padding, padding),
					size = Size(size.width - padding * 2, size.height - padding * 2)
				),
				startAngleDegrees = 0f,
				sweepAngleDegrees = 360f
			)
		}

		drawPath(
			path = path,
			color = DefaultOrbitColor,
			style = Stroke(width = 5f)
		)

		val measure = PathMeasure().apply {
			setPath(path, false)
		}
	}
}

internal fun DrawScope.drawEarth(x: Float, y: Float) {
	drawCircle(Color.Blue, radius = 15f, center = Offset(x, y))
}
