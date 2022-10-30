package me.onebone.scidatepick.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.onebone.scidatepick.ScientificDatePicker
import me.onebone.scidatepick.app.ui.theme.ScientificDatepickerTheme
import java.time.LocalDate

class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			ScientificDatepickerTheme {
				// A surface container using the 'background' color from the theme
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colors.background
				) {
					var date by remember { mutableStateOf(LocalDate.now()) }

					ScientificDatePicker(
						modifier = Modifier
							.background(Color.Black)
							.padding(16.dp)
							.fillMaxWidth()
							.aspectRatio(2.5f),
						date = date,
						onDateChange = {
							date = it
							println(it)
						}
					)
				}
			}
		}
	}
}
