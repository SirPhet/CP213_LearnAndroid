package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part1AnimationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            _523LabLearnAndroidTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        LikeButton()
                    }
                }
            }
        }
    }
}

@Composable
fun LikeButton() {
    var isLiked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // 1. Scale animation (ขยายแล้วกลับมาขนาดเดิมเมื่อกด/ปล่อย)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.2f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )

    // 2. Color animation (เปลี่ยนสีเทาเป็นสีชมพู)
    val pinkColor = Color(0xFFF48FB1)
    val backgroundColor by animateColorAsState(
        targetValue = if (isLiked) pinkColor else Color.LightGray,
        label = "color"
    )

    Button(
        onClick = { isLiked = !isLiked },
        interactionSource = interactionSource,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        modifier = Modifier.scale(scale)
    ) {
        // 3. AnimatedVisibility (แสดง Icon เมื่อสถานะเป็น Liked)
        AnimatedVisibility(visible = isLiked) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Heart Icon",
                modifier = Modifier.padding(end = 8.dp),
                tint = Color.White
            )
        }
        Text(
            text = if (isLiked) "Liked" else "Like",
            color = if (isLiked) Color.White else Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LikeButtonPreview() {
    _523LabLearnAndroidTheme {
        LikeButton()
    }
}