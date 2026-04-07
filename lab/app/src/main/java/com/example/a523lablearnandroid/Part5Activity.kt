package com.example.a523lablearnandroid

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

// ViewModel สำหรับโชว์ LaunchedEffect (One-time event)
class Part5ViewModel : ViewModel() {
    // ใช้ SharedFlow หรือ Channel สำหรับเหตุการณ์ที่เกิดขึ้นครั้งเดียว (One-time event)
    // เหตุผล: ถ้ายัดใส่ State ปกติ พอหน้าจอพลิกแกนตั้ง/นอน มันจะเกิดการ Recompose แล้วโชว์ Snackbar ซ้ำรัวๆ
    private val _errorEventFlow = MutableSharedFlow<String>()
    val errorEventFlow = _errorEventFlow.asSharedFlow()

    fun triggerError() {
        // จำลองการโหลดอะไรบางอย่างแล้วพัง
        viewModelScope.launch {
            _errorEventFlow.emit("🚨 เกิดข้อผิดพลาดจากเซิร์ฟเวอร์ (Error 500)!")
        }
    }
}

class Part5Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                // ประกาศ SnackbarHostState เป็น State หลักที่ Scaffold ต้องรับไป
                val snackbarHostState = remember { SnackbarHostState() }
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
                ) { innerPadding ->
                    SideEffectShowcaseScreen(
                        modifier = Modifier.padding(innerPadding),
                        snackbarHostState = snackbarHostState
                    )
                }
            }
        }
    }
}

@Composable
fun SideEffectShowcaseScreen(
    modifier: Modifier = Modifier,
    viewModel: Part5ViewModel = viewModel(),
    snackbarHostState: SnackbarHostState
) {
    // ----------------------------------------------------
    // 1. LaunchedEffect (Observe One-Time Event จาก ViewModel)
    // ----------------------------------------------------
    LaunchedEffect(Unit) {
        viewModel.errorEventFlow.collectLatest { errorMessage ->
            // กลไกนี้เรียกโชว์ Snackbar ได้สบายใจเฉิบโดยไม่กระตุก UI
            // และแสดงแค่ตอน Event เด้งมาจริงๆ เท่านั้น
            snackbarHostState.showSnackbar(message = errorMessage)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Compose Side Effects",
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )

        SideEffectCard(
            title = "1. LaunchedEffect & Flow",
            desc = "ดักจับ Event จาก ViewModel (SharedFlow) แล้วยิง Snackbar แจ้งเตือนข้อผิดพลาดทันที (ลองกดแล้วดูจอด้านล่าง)"
        ) {
            Button(onClick = { viewModel.triggerError() }, modifier = Modifier.fillMaxWidth()) {
                Text("Trigger Error (ViewModel Event)")
            }
        }

        HorizontalDivider()

        // ----------------------------------------------------
        // 2. rememberCoroutineScope Showcase
        // ----------------------------------------------------
        val scope = rememberCoroutineScope()
        SideEffectCard(
            title = "2. rememberCoroutineScope",
            desc = "เอาไว้เสก Coroutine ให้ผูกติดกับ Composable สำหรับใช้ในปุ่ม (onClick Event) หรือแอนิเมชันนอก LaunchedEffect"
        ) {
            Button(
                onClick = {
                    scope.launch { 
                        snackbarHostState.showSnackbar("✅ โชว์สำเร็จแล้วจากคำสั่งใน onClick!") 
                    }
                }, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Show Success Snackbar (onClick)")
            }
        }

        HorizontalDivider()

        // ----------------------------------------------------
        // 3. DisposableEffect Showcase
        // ----------------------------------------------------
        val lifecycleOwner = LocalLifecycleOwner.current
        var currentLifecycleState by remember { mutableStateOf("Unknown") }

        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                currentLifecycleState = event.name
            }
            lifecycleOwner.lifecycle.addObserver(observer)

            onDispose {
                // เก็บกวาด (Unregister) เมื่อ Composable ออกจากหน้าจอ (Destroy)
                // ป้องกัน Memory Leak
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }

        SideEffectCard(
            title = "3. DisposableEffect",
            desc = "จัดการ Resource ภายนอก พอเลิกใช้ก็เข้าบล็อก onDispose ทิ้งไปซะ (เช่น Lifecycle Observer, BroadcastReceiver, EventBus)"
        ) {
            Text("Lifecycle Event ล่าสุดของหน้านี้คือ:\n$currentLifecycleState", fontWeight = FontWeight.Bold)
        }

        HorizontalDivider()

        // ----------------------------------------------------
        // 4. produceState Showcase
        // ----------------------------------------------------
        // เป็นการเปิด Coroutine ภายในที่คืนค่าออกมาเป็น State เหมาะกับทำ Flow ที่ไม่ต้องพึ่ง ViewModel (เช่น Counter เล็กๆ หรือ โหลดภาพ)
        val timer by produceState(initialValue = 0) {
            while (true) {
                delay(1000L)
                value += 1
            }
        }

        SideEffectCard(
            title = "4. produceState",
            desc = "ผูกค่า State เข้ากับ Coroutine โดยตรง ทำหน้าที่นับเวลาสดๆ จาก 0 ไปเรื่อยๆ เป็น State แบบออโต้"
        ) {
            Text("Timer เปิดทิ้งไว้วินาทีที่: $timer ⏱️", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        }

        HorizontalDivider()

        // ----------------------------------------------------
        // 5. SideEffect Showcase
        // ----------------------------------------------------
        var recomposeCounter by remember { mutableIntStateOf(0) }
        
        SideEffect {
            // โค้ดตรงนี้จะรัน "หลังจากที่ Recompose สร้าง UI เสร็จแล้ว" (มักเอาไว้ส่งค่าไปหา Non-Compose Code หรือทำ Analytics/Logging)
            Log.d("SideEffectDemo", "หน้าจอนี้ถูก Recompose ไปแล้ว $recomposeCounter ครั้ง (เช็คได้ใน Logcat ยิ่ง Timer เดิน ยิ่งอัปเดต)")
        }

        SideEffectCard(
            title = "5. SideEffect (Logging / Sync ข้อมูล)",
            desc = "รันคำสั่งทำงานทุกครั้งที่หน้าจอเกิดการเรนเดอร์ใหม่ (Recomposition) ลองกดปุ่มด้านล่างเพื่อบังคับเรนเดอร์ดูครับ"
        ) {
            Button(onClick = { recomposeCounter++ }, modifier = Modifier.fillMaxWidth()) {
                Text("บังคับ Recompose หน้าจอ (กดไปแล้ว $recomposeCounter ครั้ง)")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SideEffectCard(title: String, desc: String, content: @Composable () -> Unit) {
    Column {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = desc, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}
