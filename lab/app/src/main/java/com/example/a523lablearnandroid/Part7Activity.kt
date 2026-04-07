package com.example.a523lablearnandroid

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityOptionsCompat
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

// 1. หน้าจอหลัก (MainActivity)
class Part7Activity : ComponentActivity() {
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
                            .padding(horizontal = 24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Transition Types",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "เลือกรูปแบบการเปิดหน้าต่าง",
                            fontSize = 16.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(40.dp))

                        // Type 1: Slide Up (ตามโจทย์)
                        TransitionButton(
                            text = "1. Slide Up (ขึ้นจากข้างล่าง)",
                            animId = 1,
                            enterAnim = R.anim.slide_in_up,
                            exitAnim = R.anim.stay,
                            buttonColor = Color(0xFFE91E63)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        // Type 2: Fade In
                        TransitionButton(
                            text = "2. Fade In (ค่อยๆ ปรากฏ)",
                            animId = 2,
                            enterAnim = android.R.anim.fade_in,
                            exitAnim = android.R.anim.fade_out,
                            buttonColor = Color(0xFF00BCD4)
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))

                        // Type 3: Slide Left (เลื่อนจากขวามาซ้าย คลาสสิก)
                        TransitionButton(
                            text = "3. Slide Left (ไถจากขวา)",
                            animId = 3,
                            enterAnim = android.R.anim.slide_in_left,  // หมายถึงหน้าจอใหม่เลื่อนเข้ามา (In) ทับฝั่งซ้าย (Left)
                            exitAnim = android.R.anim.slide_out_right, // หมายถึงหน้าจอเก่าเลื่อนหลบ (Out) ไปทางขวา (Right)
                            buttonColor = Color(0xFF8BC34A)
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun TransitionButton(text: String, animId: Int, enterAnim: Int, exitAnim: Int, buttonColor: Color) {
        Button(
            onClick = {
                // 1. สร้าง Intent ไปหา DetailActivity พร้อมแนบ String Extra
                val intent = Intent(this@Part7Activity, Part7DetailActivity::class.java).apply {
                    putExtra("EXTRA_MESSAGE", "คุณเปิดเข้ามาด้วยท่า: $text")
                    putExtra("EXTRA_ANIM_TYPE", animId) // แนบ ID อนิเมชั่นไป เพื่อให้ขากลับมันใช้ท่าสอดคล้องกัน
                }
                
                // 2. ใช้คำสั่งจัดการ Transition
                val options = ActivityOptionsCompat.makeCustomAnimation(
                    this@Part7Activity,
                    enterAnim, // แอนิเมชั่นของหน้าใหม่
                    exitAnim   // แอนิเมชั่นของหน้าเก่า
                )
                startActivity(intent, options.toBundle())
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
        ) {
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}

// 2. หน้าต่างรายละเอียด (DetailActivity)
class Part7DetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // รับค่า String จากหน้าแรก
        val extraMessage = intent.getStringExtra("EXTRA_MESSAGE") ?: "No Message"

        setContent {
            _523LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(innerPadding)
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Detail Screen",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = extraMessage,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        // 3. ปุ่ม Close เมื่อกดจะดึงแอนิเมชั่นขาออกย้อนกลับไป
                        Button(
                            onClick = { finishAndAnimate() },
                            modifier = Modifier.fillMaxWidth().height(56.dp)
                        ) {
                            Text("❌ Close Window")
                        }
                    }
                }
            }
        }
    }

    // ฟังก์ชันช่วยจัดการ Animation ขาออก (ปิดหน้า)
    private fun finishAndAnimate() {
        finish() // เรียกปิดหน้าจอตามโจทย์
        
        val animType = intent.getIntExtra("EXTRA_ANIM_TYPE", 1)
        
        // สำหรับ Android 14 (API 34+) มีคำสั่งใหม่ overrideActivityTransition
        // สำหรับรุ่นเก่ากว่านั้น ใช้คำสั่งคลาสสิก overridePendingTransition
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            when (animType) {
                1 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.stay, R.anim.slide_out_down)
                2 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, android.R.anim.fade_in, android.R.anim.fade_out)
                3 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        } else {
            when (animType) {
                1 -> overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
                2 -> overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                3 -> overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }

    // ดักจับเวลากดปุ่มย้อนกลับที่มือถือ เพื่อให้เล่นแอนิเมชั่นขาออกเหมือนกับกดปุ่ม Close เป๊ะๆ
    override fun finish() {
        super.finish()
        
        val animType = intent.getIntExtra("EXTRA_ANIM_TYPE", 1)
        
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            when (animType) {
                1 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, R.anim.stay, R.anim.slide_out_down)
                2 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, android.R.anim.fade_in, android.R.anim.fade_out)
                3 -> overrideActivityTransition(android.app.Activity.OVERRIDE_TRANSITION_CLOSE, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        } else {
            when (animType) {
                1 -> overridePendingTransition(R.anim.stay, R.anim.slide_out_down)
                2 -> overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                3 -> overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            }
        }
    }
}
