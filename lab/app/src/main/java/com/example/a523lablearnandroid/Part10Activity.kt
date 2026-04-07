package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part10Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "App Widget Concept",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        // Fake Widget Mockup ออกแบบหน้าตาสมมติว่านี่คือวิดเจ็ต
                        Box(
                            modifier = Modifier
                                .size(240.dp, 120.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFE3F2FD))
                                .padding(16.dp)
                        ) {
                            Column(modifier = Modifier.fillMaxSize()) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.Blue)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("My Widget (Mock)", fontWeight = FontWeight.SemiBold, color = Color.DarkGray)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Text("DATA PREVIEW", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold, color = Color.Blue)
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(40.dp))
                        
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
                            Text(
                                text = "💡 แนวคิดการทำ App Widget ในโลกของ Compose\n\n1. การทำ Widget ด้วย Compose จริงๆ เราจะไม่ใช้โค้ดในโปรเจกต์หลักตรงๆ แต่ต้องพึ่งไลบรารีชื่อ 'Jetpack Glance'\n\n2. หน้าที่ของ Glance จะแปลกกว่าเพื่อน คือมันจะแปลโค้ดต้นฉบับสไตล์ Compose ของเรา ให้แปลงร่างกลายเป็น RemoteViews (XML ตัวเก่าดั้งเดิมที่หน้าจอ Home Screen ของมือถือรู้จัก)\n\n3. การสร้าง Widget จำเป็นต้องขอ Permission จากระบบ และติดตั้งตัวรับสัญญาณ (Receiver) ใน AndroidManifest เสมือนว่า Widget คือแอพย่อยๆ ตัวหนึ่งที่คอยอัพเดทค่าลอยตัวบนหน้าจอของคุณ",
                                modifier = Modifier.padding(16.dp),
                                lineHeight = 24.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
