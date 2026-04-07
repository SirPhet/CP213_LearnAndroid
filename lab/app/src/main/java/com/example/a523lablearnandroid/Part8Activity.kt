package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part8Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResponsiveProfileScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun ResponsiveProfileScreen(modifier: Modifier = Modifier) {
    // 1. พระเอกของงาน: BoxWithConstraints จะให้ค่า constraint ต่างๆ ณ ขนาดหน้าจอปัจจุบัน
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // อ้างอิงตัวแปร `maxWidth` ที่ได้มาจาก Scope ของ BoxWithConstraints
        if (maxWidth < 600.dp) {
            // 2. ถ้าจอกว้างน้อยกว่า 600.dp (หน้าจอมือถือแนวตั้ง) -> ใช้เนื้อหาแนวตั้งแบบ Column
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileImage(modifier = Modifier.size(150.dp))
                Spacer(modifier = Modifier.height(24.dp))
                ProfileDetails(modifier = Modifier.fillMaxWidth(), isLandscape = false)
            }
        } else {
            // 3. ถ้าจอกว้างมากกว่าหรือเท่ากับ 600.dp (หน้าจอแนวนอนแนวนอน หรือ แท็บเล็ต) -> ใช้เนื้อหาแนวนอนแบบ Row
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileImage(modifier = Modifier.size(200.dp))
                Spacer(modifier = Modifier.width(32.dp))
                ProfileDetails(
                    modifier = Modifier.weight(1f), // เทพื้นที่ว่างที่เหลือให้ข้อมูลส่วนตัว
                    isLandscape = true
                ) 
            }
        }
    }
}

@Composable
fun ProfileImage(modifier: Modifier = Modifier) {
    // กล่องสมมติสีเทาแทน 'รูปโปรไฟล์'
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Profile\nPic", 
            color = Color.DarkGray, 
            fontSize = 20.sp, 
            fontWeight = FontWeight.Bold, 
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ProfileDetails(modifier: Modifier = Modifier, isLandscape: Boolean) {
    // Text อธิบาย 'ข้อมูลส่วนตัว' โดยปรับการจัดหน้าให้สอดคล้องกับ Layout
    Column(
        modifier = modifier,
        horizontalAlignment = if (isLandscape) Alignment.Start else Alignment.CenterHorizontally
    ) {
        Text(
            text = "JOHN DOE", 
            style = MaterialTheme.typography.headlineLarge, 
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Senior Android Developer", 
            style = MaterialTheme.typography.bodyLarge, 
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Passionate about creating beautiful, responsive Android applications using Jetpack Compose. Constantly exploring new architectures and UI patterns to push boundaries.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = if (isLandscape) TextAlign.Start else TextAlign.Center,
            lineHeight = 24.sp
        )
    }
}
