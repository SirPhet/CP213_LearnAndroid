package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part9Activity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                // 1. สร้าง ScrollBehavior ที่มีนิสัย "หดตัวลง (exit) เมื่อผู้ใช้เลื่อนลง จนกว่าจะเหลือแค่แถบเล็กๆ (collapsed)"
                val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
                
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        // 2. เสียบ nestedScrollConnection เข้าที่ร่างหลัก เพื่อให้ Scroll ฝั่ง LazyColumn สะท้อนมาที่ TopAppBar
                        .nestedScroll(scrollBehavior.nestedScrollConnection),
                    topBar = {
                        LargeTopAppBar(
                            title = { Text("Collapsing Toolbar", fontWeight = FontWeight.Bold) },
                            scrollBehavior = scrollBehavior,
                            colors = TopAppBarDefaults.largeTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                titleContentColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        item {
                            Text(
                                text = "CONCEPT EXPLORATION",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                                Text(
                                    text = "Collapsing Toolbar ทำงานด้วยระบบ Nested Scrolling ของ Material 3\n\nเมื่อเราไถลิสต์เลื่อนลง (Scroll Down เพื่อดูของข้างล่าง) ลิสต์จะไปผลักดันสายพานของ nestedScrollConnection ทำให้ TopAppBar ที่รออยู่ข้างบนรับรู้ความเคลื่อนไหว และค่อยๆ หดตัวบีบแบนลงอย่างราบรื่น",
                                    modifier = Modifier.padding(16.dp),
                                    lineHeight = 24.sp
                                )
                            }
                        }
                        
                        items(50) { index ->
                            Text(
                                text = "Item ข้อมูลจำลองที่ #${index + 1}",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 12.dp)
                            )
                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }
}
