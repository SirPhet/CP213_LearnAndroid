package com.example.a523lablearnandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalProvider
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MenuActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, MainActivity::class.java))
                }) {
                    Text(text = "RPGCardActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, MainActivity2::class.java))
                }) {
                    Text(text = "RPGCardActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, PokedexActivity::class.java))
                }) {
                    Text(text = "RPGCardActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, SharePerferencesActivity::class.java))
                }) {
                    Text(text = "SharePerferencesActivity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, GalleryActivity::class.java))
                }) {
                    Text(text = "GalleryActivity (Image Picker)")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, SensorActivity::class.java))
                }) {
                    Text(text = "Sensor Activity (Task 2 & 3)")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part1AnimationActivity::class.java))
                }) {
                    Text(text = "Part 1 Animation Activity")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part2Activity::class.java))
                }) {
                    Text(text = "Part 2 Contact List")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part3Activity::class.java))
                }) {
                    Text(text = "Part 3 Donut Chart")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part4Activity::class.java))
                }) {
                    Text(text = "Part 4 Swipe Dismiss")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part5Activity::class.java))
                }) {
                    Text(text = "Part 5 Side Effects (Snackbar/Launch)")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part6Activity::class.java))
                }) {
                    Text(text = "Part 6 In-App WebView")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part7Activity::class.java))
                }) {
                    Text(text = "Part 7 Activity Transition")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part8Activity::class.java))
                }) {
                    Text(text = "Part 8 Responsive Design")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part9Activity::class.java))
                }) {
                    Text(text = "Part 9 Collapsing Toolbar")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part10Activity::class.java))
                }) {
                    Text(text = "Part 10 App Widget")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part11Activity::class.java))
                }) {
                    Text(text = "Part 11 Skeleton Loading")
                }
                Button(onClick = {
                    startActivity(Intent(this@MenuActivity, Part12Activity::class.java))
                }) {
                    Text(text = "Part 12 Bottom Sheet & Dialog")
                }
            }
        }
    }
}