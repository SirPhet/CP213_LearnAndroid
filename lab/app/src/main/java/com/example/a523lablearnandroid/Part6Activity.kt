package com.example.a523lablearnandroid

import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

// 1. สร้าง ViewModel ที่มี State เก็บค่า URL String (ค่าเริ่มต้นคือ Google)
class Part6ViewModel : ViewModel() {
    private val _urlState = MutableStateFlow("https://www.google.com")
    val urlState: StateFlow<String> = _urlState.asStateFlow()

    fun updateUrl(newUrl: String) {
        // หากผู้ใช้พิมพ์แค่โดเมน ให้เติม https:// ให้แบบอัตโนมัติ
        val validUrl = if (!newUrl.startsWith("http://") && !newUrl.startsWith("https://")) {
            "https://$newUrl"
        } else {
            newUrl
        }
        _urlState.value = validUrl
    }
}

class Part6Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WebViewScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    viewModel: Part6ViewModel = viewModel()
) {
    // สังเกตค่า URL จาก ViewModel (ถ้าเปลี่ยนหน้าเว็บจะเปลี่ยนตาม)
    val currentUrl by viewModel.urlState.collectAsState()
    
    // State ย่อยสำหรับช่องกรอก Text (TextField input)
    var inputText by remember { mutableStateOf(currentUrl) }

    Column(modifier = modifier.fillMaxSize()) {
        // 4. สร้าง TextField ด้านบนหน้าจอสำหรับพิมพ์ URL และปุ่ม 'Go'
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier.weight(1f),
                label = { Text("Enter Website URL") },
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.updateUrl(inputText) }) {
                Text("Go")
            }
        }

        // 2 & 3. ใช้ AndroidView เพื่อสร้าง android.webkit.WebView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            // block: factory -> ทำงานแค่ "ครั้งแรก" ที่ระบบวาด View ขึ้นมา เพื่อให้เราสร้างและดัดแปลงสเปคของ View จากโลก Android ปกติ
            factory = { context ->
                WebView(context).apply {
                    // ตั้งค่า webViewClient ให้หน้าต่างเว็บเปิดในแอปเท่านั้น ไม่เด้งออกไปเปิดแอพเบราว์เซอร์ Chrome นอกแอป
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true // เป็นการอนุญาตให้รัน JS ภายในแอป (จำเป็นมากสำหรับเว็บสมัยใหม่)
                    loadUrl(currentUrl)
                }
            },
            // block: update -> ทำงานทุกครั้งที่ State จากโลก Compose ด้านนอก (currentUrl) มีการเปลี่ยนแปลงใดๆ
            // กฎเหล็ก: เราห้ามสร้าง View ใหม่ในบล็อกนี้เด็ดขาด แต่เราสั่งให้ตัวแปร webView รับค่าเข้าไปทำงานทับของเดิมได้
            update = { webView ->
                // เช็คก่อนว่า URL ใหม่ซ้ำกับที่กำลังเปิดอยู่ไหม ป้องกันปัญหาโหลดหน้าเดิมซ้ำไปมา (Infinite Loop)
                if (webView.url != currentUrl && webView.originalUrl != currentUrl) {
                    webView.loadUrl(currentUrl)
                }
            }
        )
    }
}
