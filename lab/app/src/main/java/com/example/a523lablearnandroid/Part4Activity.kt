package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme
import kotlin.math.roundToInt

class Part4ViewModel : ViewModel() {
    private val _todoItems = mutableStateListOf(
        "Swipe to Dismiss ข้อที่ 1",
        "Swipe to Dismiss ข้อที่ 2",
        "Swipe to Dismiss ข้อที่ 3"
    )
    val todoItems: List<String> get() = _todoItems

    fun removeItem(item: String) {
        _todoItems.remove(item)
    }
}

class Part4Activity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GestureShowcaseScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestureShowcaseScreen(
    modifier: Modifier = Modifier,
    viewModel: Part4ViewModel = viewModel()
) {
    // ใช้ LazyColumn เป็น Root กลางจะได้เลื่อนจอลงไปดูท่าทางด้านล่างๆ ได้โดยไม่บัค
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Gestures Showcase",
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // 1. แตะปกติ แตะเบิ้ล หรือกดค้าง
        item {
            GestureCard("1. Tap / Double Tap / Long Press") {
                TapGestureExample()
            }
        }

        // 2. ลากวัตถุแบบอิสระ
        item {
            GestureCard("2. Drag (ลากกล่องไปมาอย่างอิสระ)") {
                DragGestureExample()
            }
        }

        // 3. ปรับขนาด หมุน ซูมพร้อมๆ กัน
        item {
            GestureCard("3. Transform (Pinch to Zoom & Rotate)") {
                TransformGestureExample()
            }
        }

        // 4. ของเดิมคือ ปัดเพื่อลบ
        item {
            Text(
                text = "4. Swipe-to-Dismiss (ปัดเพื่อลบ)",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        items(
            items = viewModel.todoItems,
            key = { it }
        ) { item ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = { dismissValue ->
                    if (dismissValue == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.removeItem(item)
                        true
                    } else {
                        false
                    }
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    val color by animateColorAsState(
                        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) Color.Red else Color.Transparent,
                        label = "dismiss_color"
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color)
                            .padding(end = 20.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.White)
                        }
                    }
                },
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 20.dp, vertical = 20.dp)
                ) {
                    Text(text = item, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GestureCard(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title, 
            fontSize = 18.sp, 
            fontWeight = FontWeight.Bold, 
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
fun TapGestureExample() {
    var text by remember { mutableStateOf("Try interacting with me!") }
    var color by remember { mutableStateOf(Color(0xFF03A9F4)) } // ฟ้า

    // detectTapGestures รวมการคลิกแตะทุกรูปแบบไว้ที่เดียว
    val pointerInputModifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onTap = {
                text = "Tapped once!"
                color = Color(0xFF4CAF50) // เขียว
            },
            onDoubleTap = {
                text = "Double tapped!"
                color = Color(0xFFFFC107) // เหลืองอำพัน
            },
            onLongPress = {
                text = "Long pressed!"
                color = Color(0xFFE91E63) // ชมพู
            }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(color)
            .then(pointerInputModifier),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
    }
}

@Composable
fun DragGestureExample() {
    // เก็บค่า Offeset X/Y ปัจจุบัน เพื่อจำว่าเราลากไปถึงไหนแล้ว
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFEEEEEE)) // พื้นหลังพื้นที่ให้ลาก
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
                .size(80.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF9C27B0)) // กล่องม่วงที่ให้ลากได้
                .pointerInput(Unit) {
                    // detectDragGestures ดักจับขณะนิ้วลากบนหน้าจอ
                    detectDragGestures { change, dragAmount ->
                        change.consume() // บริโภค Event ไม่ให้หลุดไป Component อื่นรบกวนการเลื่อนหน้าจอ
                        offsetX += dragAmount.x
                        offsetY += dragAmount.y
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            Text("Drag Me", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TransformGestureExample() {
    var scale by remember { mutableFloatStateOf(1f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var offset by remember { mutableStateOf(androidx.compose.ui.geometry.Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF222222)), // กรอบดำ
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    rotationZ = rotation,
                    translationX = offset.x,
                    translationY = offset.y
                )
                .pointerInput(Unit) {
                    // detectTransformGestures จับได้ทั้งสองนิ้วซูม, หมุน(Rotate), และเลื่อน (Pan) พร้อมๆ กันไปเลย
                    detectTransformGestures { _, pan, zoom, panRotation ->
                        scale = (scale * zoom).coerceIn(0.5f, 5f) // จำกัดให้ซูมได้ระหว่าง 50% ถึง 500%
                        rotation += panRotation
                        offset += pan
                    }
                }
                .size(150.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFF5722)), // กล่องสีส้มสะท้อนแสง
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Pinch to Zoom\n&\nRotate Me",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
