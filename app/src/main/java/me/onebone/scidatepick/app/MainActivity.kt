package me.onebone.scidatepick.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.onebone.scidatepick.ScientificDatePicker
import me.onebone.scidatepick.app.ui.theme.ScientificDatepickerTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

					Column(
						modifier = Modifier.fillMaxSize().background(Color.Black),
						verticalArrangement = Arrangement.Center
					) {
						ScientificDatePicker(
							modifier = Modifier
								.padding(16.dp)
								.fillMaxWidth()
								.aspectRatio(1.2f),
							date = date,
							onDateChange = { date = it }
						)

						Text(
							modifier = Modifier.align(Alignment.CenterHorizontally),
							text = date.format(DateTimeFormatter.ISO_DATE),
							fontSize = 18.sp,
							color = Color.White
						)
					}
				}
			}
		}
	}
}
