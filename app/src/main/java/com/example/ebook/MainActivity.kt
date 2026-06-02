package com.example.ebook

import android.graphics.PathMeasure
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

enum class ScrollDirection { Up, Down }
enum class AnimationPhase { Scrolling, Spiral }

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
                    val books1 = column1Books
                    val books2 = column2Books
                    val books3 = column3Books

                    val columnStep = BookWidth + ColumnGap
                    val columnCount = 2
                    var containerSize by remember { mutableStateOf(IntSize.Zero) }
                    var phase by remember { mutableStateOf(AnimationPhase.Scrolling) }
                    var columnsFinished by remember { mutableIntStateOf(0) }

                    LaunchedEffect(columnsFinished) {
                        if (columnsFinished >= columnCount) {
                            phase = AnimationPhase.Spiral
                        }
                    }

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
                                books = books1,
                                phase = phase,
                                onScrollComplete = { columnsFinished++ }
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
                                books = books2,
                                phase = phase,
                                onScrollComplete = { columnsFinished++ }
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
                                books = books3,
                                phase = phase,
                                onScrollComplete = { columnsFinished++ }
                            )
//                            PathFollower(
//                                containerSize = containerSize,
//                                xOffset = columnStep * 3,
//                                duration = 3000,
//                                scrollRepetitions = 3,
//                                startProgress = 0.4f,
//                                endProgress = 0.8f,
//                                direction = ScrollDirection.Up,
//                                bookWidth = BookWidth,
//                                bookHeight = BookHeight,
//                                gap = BookGap,
//                                images = shuffled2,
//                                phase = AnimationPhase.Spiral,
//                                onScrollComplete = { columnsFinished++ }
//                            )
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
    books: List<Book>,
    duration: Int,
    startProgress: Float = 0f,
    endProgress: Float = 1f,
    direction: ScrollDirection = ScrollDirection.Down,
    bookWidth: Dp,
    bookHeight: Dp,
    gap: Dp,
    phase: AnimationPhase,
    onScrollComplete: () -> Unit
) {
    val density = LocalDensity.current
    val scrollProgress = remember { Animatable(startProgress) }
    val spiralProgresses = remember(books.size) { List(books.size) { Animatable(0f) } }

    val xOffsetPx = with(density) { xOffset.toPx().roundToInt() }
    val bookWidthPx = with(density) { bookWidth.toPx() }
    val stepPx = with(density) { (bookHeight + gap).toPx() }
    val bookHPx = with(density) { bookHeight.toPx() }
    val loop = stepPx * books.size

    LaunchedEffect(Unit) {
        scrollProgress.animateTo(
            endProgress,
            animationSpec = tween(durationMillis = duration, easing = FastOutSlowInEasing)
        )
        onScrollComplete()
    }

    LaunchedEffect(phase) {
        if (phase == AnimationPhase.Spiral) {
            spiralProgresses.forEachIndexed { index, progress ->
                launch {
                    val order = books[index].slot ?: index
                    delay(order * 100L)
                    progress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(
                            durationMillis = 6000,
                            easing = FastOutSlowInEasing
                        )
                    )
                }
            }
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val cx = xOffsetPx + bookWidthPx / 2
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(cx, 0f)
            lineTo(cx, containerSize.height.toFloat())
        }
        drawPath(path, Color.Transparent, style = Stroke(width = 4.dp.toPx()))

        if (phase == AnimationPhase.Spiral) {
            books.forEachIndexed { index, book ->
                val rawY = (index * stepPx + endProgress * loop) % loop
                val y = when (direction) {
                    ScrollDirection.Down -> rawY
                    ScrollDirection.Up -> loop - rawY
                }
                val scrollX = xOffsetPx.toFloat()
                val scrollY = y - bookHPx
                val spiralEndX = book.spiralAnimationX
                val spiralEndY = book.spiralAnimationY

                if (spiralEndX != null && spiralEndY != null) {
                    val controlPointX = book.controlPointX ?: (scrollX + 300f)
                    val controlPointY = book.controlPointY ?: (scrollY - 400f)
                    val controlPoint2X = book.controlPoint2X ?: (scrollX + 150f)
                    val controlPoint2Y = book.controlPoint2Y ?: (scrollY - 200f)

                    val spiralPath = androidx.compose.ui.graphics.Path().apply {
                        moveTo(scrollX, scrollY)
                        cubicTo(
                            controlPointX, controlPointY,
                            controlPoint2X, controlPoint2Y,
                            spiralEndX, spiralEndY
                        )
                    }
                    drawPath(spiralPath, Color.Transparent, style = Stroke(width = 4.dp.toPx()))
                }
            }
        }
    }

    books.forEachIndexed { index, book ->
        val scrollValue =
            if (phase == AnimationPhase.Spiral) endProgress else scrollProgress.value

        val rawY = (index * stepPx + scrollValue * loop) % loop
        val y = when (direction) {
            ScrollDirection.Down -> rawY
            ScrollDirection.Up -> loop - rawY
        }
        val scrollX = xOffsetPx.toFloat()
        val scrollY = y - bookHPx

        val displayX: Float
        val displayY: Float

        val spiralEndX = book.spiralAnimationX
        val spiralEndY = book.spiralAnimationY

        if (phase == AnimationPhase.Spiral && spiralEndX != null && spiralEndY != null) {
            val t = spiralProgresses[index].value
            val controlPointX = book.controlPointX ?: (scrollX + 300f)
            val controlPointY = book.controlPointY ?: (scrollY - 400f)
            val controlPoint2X = book.controlPoint2X ?: (scrollX + 150f)
            val controlPoint2Y = book.controlPoint2Y ?: (scrollY - 200f)
            val path = remember(scrollX, scrollY, spiralEndX, spiralEndY) {
                android.graphics.Path().apply {
                    moveTo(scrollX, scrollY)
                    cubicTo(
                        controlPointX, controlPointY,
                        controlPoint2X, controlPoint2Y,
                        spiralEndX, spiralEndY
                    )
                }
            }

            val pathMeasure = remember(path) { PathMeasure(path, false) }
            val pos = remember { FloatArray(2) }
            pathMeasure.getPosTan(pathMeasure.length * t, pos, null)

            displayX = pos[0]
            displayY = pos[1]
        } else {
            displayX = scrollX
            displayY = scrollY
        }

        Image(
            painter = painterResource(id = book.frontCover),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset { IntOffset(displayX.roundToInt(), displayY.roundToInt()) }
                .size(bookWidth, bookHeight)
        )
    }
}