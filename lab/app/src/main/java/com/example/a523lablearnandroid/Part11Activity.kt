package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

// Extension function เพื่อสร้างพู่กันวาดสีแบบเลื่อนผ่านรัวๆ (Shimmer)
fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "ShimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "ShimmerAnimation"
    )
    
    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.4f), 
                Color.LightGray.copy(alpha = 0.8f), 
                Color.LightGray.copy(alpha = 0.4f)
            ),
            start = Offset(translateAnim - 500f, translateAnim - 500f),
            end = Offset(translateAnim, translateAnim)
        )
    )
}

class Part11Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                // สร้างตัวแปร State เพื่อจำลองว่าเซิร์ฟเวอร์ยังส่งของมาไม่เสร็จ
                var isLoading by remember { mutableStateOf(true) }

                // รอ 3 วินาทีแล้วปรับ State จาก Loading ให้เป็นโชว์ของจริง
                LaunchedEffect(Unit) {
                    delay(3000L)
                    isLoading = false
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding).padding(16.dp)) {
                        Text(
                            text = "Skeleton Loading",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Card {
                            Text(
                                text = "CONCEPT: Skeleton Loading คือการแสดงอวัยวะ 'โครงกระดูก' ของ UI ออกมาก่อนในช่วงที่แอปกำลังหมุนติ้วๆ ดึงข้อมูลเซิร์ฟเวอร์ ศาสตร์นี้ช่วยลดความหงุดหงิดใจของผู้ใช้ ทำให้แอปดูโหลดเร็วขึ้น\n\nการใช้เทคนิค Brush.linearGradient ควบคู่กับระบบ animateFloat แบบไม่มีวันหยุดพัก (Infinite) ช่วยเราเสกแสงเงาแวววาว (Shimmer) วิ่งพาดผ่านกรอบข้อความได้อย่างเนียนตา",
                                modifier = Modifier.padding(16.dp),
                                lineHeight = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(8) { index ->
                                if (isLoading) {
                                    SkeletonListItem()
                                } else {
                                    RealListItem(index + 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SkeletonListItem() {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        // รูปโปรไฟล์กระดูก
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            // หัวข้อเนื้อหากระดูก
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(8.dp))
            // รายละเอียดกระดูก
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .height(16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
fun RealListItem(number: Int) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        // รูปโปรไฟล์มีสี
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Text("IMG", fontSize = 14.sp, color = Color.White)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text("สินค้าตัวจริง หมายเลข #$number", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("พร้อมจำหน่ายและจัดส่งแล้ว", color = Color.DarkGray)
        }
    }
}
