package com.example.ebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.ebook.ui.theme.EbookTheme
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

enum class ScrollDirection { Up, Down }

private val BookWidth = 90.dp
private val BookHeight = 140.dp
private val BookGap = 12.dp
private val ColumnGap = 20.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EbookTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val books = listOf(
                        R.drawable.front_cover,
                        R.drawable.the_law_of_human_nature_front_cover,
                        R.drawable.the_mountain_is_you_front_cover,
                        R.drawable.the_picture_of_dorian_gray_front_cover,
                        R.drawable.without_a_trace_front_cover,
                        R.drawable.front_cover,
                        R.drawable.the_law_of_human_nature_front_cover,
                        R.drawable.the_mountain_is_you_front_cover,
                        R.drawable.the_picture_of_dorian_gray_front_cover,
                        R.drawable.without_a_trace_front_cover,
                        R.drawable.front_cover,
                        R.drawable.the_law_of_human_nature_front_cover,
                        R.drawable.the_mountain_is_you_front_cover,
                        R.drawable.the_picture_of_dorian_gray_front_cover,
                        R.drawable.without_a_trace_front_cover,
                        R.drawable.the_picture_of_dorian_gray_front_cover
                    )

                    val columnStep = BookWidth + ColumnGap

                    var containerSize by remember { mutableStateOf(IntSize.Zero) }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .background(Color(0xFF101418))
                            .clipToBounds()
                    ) {
                        val angle = -15f
                        val radians = Math.toRadians(angle.toDouble())
                        val scale = abs(cos(radians)).toFloat() + abs(sin(radians)).toFloat()

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationZ = angle
                                    scaleX = scale
                                    scaleY = scale
                                    translationX = 250f
                                }
                                .onSizeChanged { containerSize = it }
                        ) {
                            PathFollower(
                                containerSize = containerSize,
                                xOffset = 0.dp,
                                duration = 3000,
                                startProgress = 0.55f,
                                endProgress = 0.6f,
                                direction = ScrollDirection.Down,
                                bookWidth = BookWidth,
                                bookHeight = BookHeight,
                                gap = BookGap,
                                images = books
                            )
                            PathFollower(
                                containerSize = containerSize,
                                xOffset = columnStep,
                                duration = 3000,
                                startProgress = 0.4f,
                                endProgress = 0.8f,
                                direction = ScrollDirection.Up,
                                bookWidth = BookWidth,
                                bookHeight = BookHeight,
                                gap = BookGap,
                                images = books.shuffled()
                            )
                            PathFollower(
                                containerSize = containerSize,
                                xOffset = columnStep * 2,
                                duration = 3000,
                                startProgress = 0.4f,
                                endProgress = 0.8f,
                                direction = ScrollDirection.Down,
                                bookWidth = BookWidth,
                                bookHeight = BookHeight,
                                gap = BookGap,
                                images = books
                            )
                            PathFollower(
                                containerSize = containerSize,
                                xOffset = columnStep * 3,
                                duration = 3000,
                                startProgress = 0.4f,
                                endProgress = 0.8f,
                                direction = ScrollDirection.Up,
                                bookWidth = BookWidth,
                                bookHeight = BookHeight,
                                gap = BookGap,
                                images = books.shuffled()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PathFollower(
    containerSize: IntSize,
    xOffset: Dp = 0.dp,
    images: List<Int>,
    duration: Int,
    startProgress: Float = 0f,
    endProgress: Float = 1f,
    direction: ScrollDirection = ScrollDirection.Down,
    bookWidth: Dp,
    bookHeight: Dp,
    gap: Dp
) {
    val density = LocalDensity.current
    val progress = remember { Animatable(startProgress) }

    val xOffsetPx = with(density) { xOffset.toPx().roundToInt() }
    val bookWidthPx = with(density) { bookWidth.toPx() }

    LaunchedEffect(Unit) {
        progress.animateTo(
            endProgress,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = duration, easing = FastOutSlowInEasing),
                RepeatMode.Restart
            )
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = xOffsetPx + bookWidthPx / 2
        val path = Path().apply {
            moveTo(centerX, 0f)
            lineTo(centerX, containerSize.height.toFloat())
        }
        drawPath(
            path = path,
            color = Color.Transparent,
            style = Stroke(width = 4.dp.toPx())
        )
    }

    val stepPx = with(density) { (bookHeight + gap).toPx() }
    val bookHPx = with(density) { bookHeight.toPx() }
    val loop = stepPx * images.size

    images.forEachIndexed { index, imageRes ->
        val rawY = (index * stepPx + progress.value * loop) % loop
        val y = when (direction) {
            ScrollDirection.Down -> rawY
            ScrollDirection.Up -> loop - rawY
        }
        val yOffset = (y - bookHPx).roundToInt()

        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset { IntOffset(xOffsetPx, yOffset) }
                .size(bookWidth, bookHeight)
        )
    }
}