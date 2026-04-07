package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part12Activity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                // สวิตช์ปิดเปิด Dialog
                var showDialog by remember { mutableStateOf(false) }
                // สวิตช์ปิดเปิด Bottom Sheet
                var showBottomSheet by remember { mutableStateOf(false) }
                // ตัวคุมสถานะอนิเมชั่นของ Bottom Sheet โดยเฉพาะ
                val sheetState = rememberModalBottomSheetState()
                
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
                            text = "Dialogs & Bottom Sheets",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(32.dp))
                        
                        Card {
                            Text(
                                text = "CONCEPT: ทั้ง Modal Bottom Sheet (ลากเปิดจากขอบล่างของหน้าจอ) และ AlertDialog (โผล่มาระหว่างกลางจอพอดี) ต่างก็มีบทบาทเป็นพฤติกรรมแจ้งความจำนงแบบ 'Overlay' ซึ่งจะขัดจังหวะการโฟกัสของผู้ใช้ \n\nใน Compose ทั้งสองตัวนี้ถูกเรียกใช้ง่ายมากแค่ใช้ตัวแปร Boolean แบบ State (เช่น `showDialog = true`) เป็นไฟสวิตช์ปิดเปิด โดยแยก Component ออกไปอยู่อย่างอิสระจากส่วนอื่นเลย",
                                modifier = Modifier.padding(16.dp),
                                lineHeight = 24.sp
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(48.dp))
                        
                        Button(onClick = { showDialog = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            Text("กดโชว์ Middle Dialog", fontSize = 18.sp)
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Button(onClick = { showBottomSheet = true }, modifier = Modifier.fillMaxWidth().height(56.dp)) {
                            Text("กดโชว์ Bottom Sheet", fontSize = 18.sp)
                        }
                    }
                    
                    // ส่วนโค้ด UI ของ Middle Dialog! จะเกิดก็ต่อเมื่อ showDialog เป็น true
                    if (showDialog) {
                        AlertDialog(
                            onDismissRequest = { showDialog = false }, // ทำงานเมื่อผู้ใช้กดแตะขอบจอดำข้างนอกทิ้ง
                            title = { Text("ลบข้อมูล?") },
                            text = { Text("คุณกำลังจะลบข้อมูลนี้ทิ้งอย่างถาวร กรุณายืนยันใช่หรือไม่?") },
                            confirmButton = {
                                TextButton(onClick = { showDialog = false }) { Text("ยืนยัน", color = MaterialTheme.colorScheme.error) }
                            },
                            dismissButton = {
                                TextButton(onClick = { showDialog = false }) { Text("ยกเลิก") }
                            }
                        )
                    }
                    
                    // ส่วนโค้ด UI ของ Modal Bottom Sheet! จะเกิดก็ต่อเมื่อ showBottomSheet เป็น true
                    if (showBottomSheet) {
                        ModalBottomSheet(
                            onDismissRequest = { showBottomSheet = false },
                            sheetState = sheetState
                        ) {
                            // พื้นที่อิสระภายใน Bottom Sheet เราจะยัด Layout อะไรลงไปตรงนี้ก็ได้ตามใจชอบ
                            Column(modifier = Modifier.fillMaxWidth().padding(24.dp).padding(bottom = 32.dp)) {
                                Text("ตัวเลือกการแชร์ข้อมูล", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(24.dp))
                                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text("แชร์ไปที่ Facebook")
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { showBottomSheet = false }, modifier = Modifier.fillMaxWidth()) {
                                    Text("แชร์ลิงก์นี้ไปให้เพื่อน (Copy Url)")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
