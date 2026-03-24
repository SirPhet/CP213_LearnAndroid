package com.example.a523lablearnandroid

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage

class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                GalleryScreen(modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun GalleryScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    
    // State to hold the selected image URI
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    
    // 3. สร้าง Launcher สำหรับแกลเลอรี
    val launcherGallery = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }
    
    // 2. สร้าง Launcher สำหรับ Permission
    val launcherPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // ถ้าอนุญาตแล้ว ให้ไปที่แกลเลอรีเพื่อเลือกรูป ("image/*")
            launcherGallery.launch("image/*")
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 5. แสดงผลรูปภาพ ด้วย Coil คู่กับคำสั่ง AsyncImage
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier.size(300.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(text = "ยังไม่ได้เลือกรูปภาพ", modifier = Modifier.padding(16.dp))
        }

        // 4. ผูกลอจิกเข้ากับปุ่มกด
        Button(onClick = {
            // ตรวจสอบเวอร์ชัน Android เพื่อใช้ Permission ให้ถูกต้อง
            val permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Manifest.permission.READ_MEDIA_IMAGES
            } else {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }

            val permissionCheckResult = ContextCompat.checkSelfPermission(context, permission)
            if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                // ถ้าผ่านแล้ว ➡️ สั่ง Launcher ของแกลเลอรีให้ทำงาน
                launcherGallery.launch("image/*")
            } else {
                // ถ้ายังไม่ผ่าน ➡️ สั่ง Launcher ของ Permission ให้ทำงาน
                launcherPermission.launch(permission)
            }
        }) {
            Text(text = "เลือกรูปภาพ")
        }
    }
}
