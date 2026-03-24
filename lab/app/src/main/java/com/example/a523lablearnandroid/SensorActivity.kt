package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class SensorActivity : ComponentActivity() {
    // นำเข้า ViewModel แบบ Lazy ผ่าน Delegation ยอดนิยมของ Jetpack
    private val sensorViewModel by viewModels<SensorViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                SensorScreen(
                    viewModel = sensorViewModel,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun SensorScreen(viewModel: SensorViewModel, modifier: Modifier = Modifier) {
    // 3. ใช้ collectAsState() เพื่อดึงข้อมูลเข้า Compose State
    val sensorValues by viewModel.sensorData.collectAsState()

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Accelerometer Sensor Data", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))
        
        // ข้อมูลจะอัปเดตอัตโนมัติเนื่องจาก StateFlow อัปเดต State ส่งตรงที่ Compose Recomposition
        Text("X: ${sensorValues[0]}", fontSize = 20.sp)
        Text("Y: ${sensorValues[1]}", fontSize = 20.sp)
        Text("Z: ${sensorValues[2]}", fontSize = 20.sp)
    }
}
