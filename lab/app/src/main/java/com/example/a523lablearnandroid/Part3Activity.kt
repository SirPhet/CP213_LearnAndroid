package com.example.a523lablearnandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.launch
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.a523lablearnandroid.utils.ui.theme._523LabLearnAndroidTheme

class Part3Activity : ComponentActivity() {
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
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(40.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Canvas Showcase",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        ChartCard("1. Animated Donut Chart (drawArc)") {
                            AnimatedDonutChart(
                                proportions = listOf(30f, 40f, 30f),
                                colors = listOf(Color(0xFFFF9800), Color(0xFF4CAF50), Color(0xFF2196F3)),
                                modifier = Modifier.size(200.dp)
                            )
                        }

                        ChartCard("2. Data Line Chart (drawPath)") {
                            LineChartCanvas(modifier = Modifier.size(300.dp, 200.dp))
                        }

                        ChartCard("3. Bar Chart (drawRect)") {
                            BarChartCanvas(modifier = Modifier.size(300.dp, 200.dp))
                        }

                        ChartCard("4. Pulse Radar (drawCircle, alpha)") {
                            PulseRadarCanvas(modifier = Modifier.size(200.dp))
                        }

                        ChartCard("5. Pacman Eating (drawArc animation)") {
                            PacmanCanvas(modifier = Modifier.size(250.dp, 100.dp))
                        }

                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun ChartCard(title: String, content: @Composable () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

// 1. Donut Chart
@Composable
fun AnimatedDonutChart(proportions: List<Float>, colors: List<Color>, modifier: Modifier = Modifier) {
    val total = proportions.sum()
    val sweepAngleTracker = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        sweepAngleTracker.animateTo(360f, animationSpec = tween(1500))
    }
    Canvas(modifier = modifier) {
        var startAngle = -90f 
        val strokeWidth = 40.dp.toPx()
        for (i in proportions.indices) {
            val sliceProportion = proportions[i]
            val color = colors.getOrElse(i) { Color.Gray }
            val fullSweepAngle = (sliceProportion / total) * 360f
            val accumulatedAngleStart = startAngle + 90f
            val allowedSweep = if (sweepAngleTracker.value > accumulatedAngleStart) {
                val remainingAngle = sweepAngleTracker.value - accumulatedAngleStart
                if (remainingAngle >= fullSweepAngle) fullSweepAngle else remainingAngle
            } else 0f
            if (allowedSweep > 0f) {
                drawArc(
                    color = color, startAngle = startAngle, sweepAngle = allowedSweep,
                    useCenter = false, style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
                    size = Size(size.width, size.height), topLeft = Offset.Zero
                )
            }
            startAngle += fullSweepAngle
        }
    }
}

// 2. Line Chart: Drawing Paths and Gradients
@Composable
fun LineChartCanvas(modifier: Modifier = Modifier) {
    val data = listOf(20f, 50f, 30f, 80f, 40f, 90f, 60f)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(1f, tween(1500))
    }
    
    Canvas(modifier = modifier) {
        val maxData = 100f
        val stepX = size.width / (data.size - 1)
        val path = Path()
        
        // Draw grid lines
        for (i in 0..4) {
            val y = size.height * (i / 4f)
            drawLine(Color.LightGray, Offset(0f, y), Offset(size.width, y), strokeWidth = 2f)
        }

        // Generate coordinates and lines
        data.forEachIndexed { index, value ->
            // Use animation progress to gradually stretch the X-axis
            val x = index * stepX * animationProgress.value
            val y = size.height - (value / maxData * size.height)
            
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
            
            // Draw points only when line arrives
            if (animationProgress.value > (index.toFloat() / data.size)) {
                drawCircle(Color.Red, radius = 6.dp.toPx(), center = Offset(x, y))
            }
        }

        drawPath(
            path = path,
            brush = Brush.linearGradient(listOf(Color.Red, Color(0xFF9C27B0))), // Red to Purple Gradient
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )
    }
}

// 3. Bar Chart: Drawing Rectangles
@Composable
fun BarChartCanvas(modifier: Modifier = Modifier) {
    val data = listOf(70f, 40f, 90f, 50f, 85f)
    val animationProgress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        animationProgress.animateTo(1f, tween(1000))
    }
    
    Canvas(modifier = modifier) {
        val barWidth = (size.width / (data.size * 2))
        val maxData = 100f
        
        data.forEachIndexed { index, value ->
            val xOffset = index * (barWidth * 2) + barWidth / 2
            // Animate height growing from bottom
            val currentBarHeight = (value / maxData * size.height) * animationProgress.value
            val startY = size.height - currentBarHeight
            
            drawRect(
                color = Color(0xFF3F51B5), // Indigo
                topLeft = Offset(xOffset, startY),
                size = Size(barWidth, currentBarHeight)
            )
        }
        
        // Base line
        drawLine(Color.Black, Offset(0f, size.height), Offset(size.width, size.height), strokeWidth = 4f)
    }
}

// 4. Pulse Radar: Animating Scales and Alphas
@Composable
fun PulseRadarCanvas(modifier: Modifier = Modifier) {
    // Continously looping animation from 0.0 to 1.0
    val pulse = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        pulse.animateTo(1f, infiniteRepeatable(tween(2000, easing = LinearEasing), RepeatMode.Restart))
    }
    
    Canvas(modifier = modifier.background(Color.Black)) { // Adding a black background for coolness
        val centerObj = Offset(size.width / 2, size.height / 2)
        val maxRadius = size.width / 2
        
        // Center dot
        drawCircle(Color.Green, radius = 5.dp.toPx(), center = centerObj)
        
        // Expanding wave 1
        val radius1 = maxRadius * pulse.value
        val alpha1 = 1f - pulse.value // fades out as it expands
        drawCircle(Color.Green.copy(alpha = alpha1), radius = radius1, center = centerObj, style = Stroke(width = 2.dp.toPx()))

        // Expanding wave 2 (Half a phase behind)
        val pulse2Val = (pulse.value + 0.5f) % 1f
        val radius2 = maxRadius * pulse2Val
        val alpha2 = 1f - pulse2Val
        drawCircle(Color.Green.copy(alpha = alpha2), radius = radius2, center = centerObj, style = Stroke(width = 2.dp.toPx()))
        
        // Crosshair Lines
        drawLine(Color.DarkGray, Offset(0f, size.height/2), Offset(size.width, size.height/2))
        drawLine(Color.DarkGray, Offset(size.width/2, 0f), Offset(size.width/2, size.height))
    }
}

// 5. Pacman: Complex arc & circle animations
@Composable
fun PacmanCanvas(modifier: Modifier = Modifier) {
    // Chomping animation (0 to 45 degrees)
    val mouthAngle = remember { Animatable(0f) }
    // Movement animation for the foods floating towards mouth
    val foodMovement = remember { Animatable(0f) }
    
    LaunchedEffect(Unit) {
        // Open and close mouth continuously
        launch { mouthAngle.animateTo(45f, infiniteRepeatable(tween(250), RepeatMode.Reverse)) }
        // Food items sliding continuously
        launch { foodMovement.animateTo(100f, infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Restart)) }
    }
    
    Canvas(modifier = modifier.background(Color(0xFF222222))) {
        val pacmanRadius = size.height / 2.5f
        
        // Draw the food items moving left
        val gap = size.width / 3
        val movingOffset = foodMovement.value * (gap / 100f)
        
        for (i in 0..4) {
            val pointX = size.width - (i * gap) + movingOffset
            // Only draw if it's within bounds and outside pacman's mouth
            if (pointX > (pacmanRadius * 1.5f) && pointX < size.width) {
                drawCircle(Color.White, radius = 6.dp.toPx(), center = Offset(pointX, size.height / 2))
            }
        }
        
        // Draw Pacman body (Arc from upper mouth edge to lower mouth edge)
        drawArc(
            color = Color.Yellow,
            startAngle = mouthAngle.value,
            sweepAngle = 360f - (mouthAngle.value * 2), // Total circle minus top and bottom gap
            useCenter = true, // Must be true to connect arc lines back to center forming a mouth
            topLeft = Offset(16.dp.toPx(), (size.height / 2) - pacmanRadius), // Center vertically
            size = Size(pacmanRadius * 2, pacmanRadius * 2)
        )
        
        // Pacman eye
        drawCircle(
            color = Color.Black,
            radius = 4.dp.toPx(),
            center = Offset(16.dp.toPx() + pacmanRadius * 1.1f, size.height / 2 - pacmanRadius * 0.5f)
        )
    }
}
