package com.example.ebook

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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

private const val ScrollDurationMs = 2400
private const val SpiralDurationMs = 1350
private const val SpiralStaggerMs = 70L

private const val TiltAngle = -15f
private const val ContentShiftX = 150f
private val BackgroundColor = Color(0xFF101418)

data class ColumnSpec(
    val books: List<Book>,
    val xOffset: Dp,
    val direction: ScrollDirection,
    val startProgress: Float,
    val endProgress: Float,
    val scrollDuration: Int = ScrollDurationMs,
)
@Composable
fun BookSpiralScreen(
    modifier: Modifier = Modifier,
    spiralTriggered: Boolean = false,
) {
    val columnStep = BookWidth + ColumnGap
    val columns = remember {
        listOf(
            ColumnSpec(column1Books, 0.dp, ScrollDirection.Down, 0.55f, 0.6f),
            ColumnSpec(column2Books, columnStep, ScrollDirection.Up, 0.4f, 0.8f),
            ColumnSpec(column3Books, columnStep * 2, ScrollDirection.Down, 0.4f, 0.8f),
            ColumnSpec(column4Books, columnStep * 3, ScrollDirection.Up, 0.4f, 0.8f),
        )
    }

    val phase = if (spiralTriggered) {
        AnimationPhase.Spiral
    } else {
        AnimationPhase.Scrolling
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundColor)
            .clipToBounds(),
    ) {
        val radians = Math.toRadians(TiltAngle.toDouble())
        val scale = abs(cos(radians)).toFloat() + abs(sin(radians)).toFloat()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = TiltAngle
                    scaleX = scale
                    scaleY = scale
                    translationX = ContentShiftX
                },
        ) {
            columns.forEach { spec ->
                BookColumn(
                    spec = spec,
                    phase = phase,
                )
            }
        }
    }
}

@Composable
private fun BookColumn(
    spec: ColumnSpec,
    phase: AnimationPhase,
) {
    val density = LocalDensity.current
    val xOffsetPx = with(density) { spec.xOffset.toPx() }
    val bookHeightPx = with(density) { BookHeight.toPx() }
    val stepPx = with(density) { (BookHeight + BookGap).toPx() }
    val loop = stepPx * spec.books.size

    val scroll = remember { Animatable(spec.startProgress) }
    val spiralProgress = remember(spec.books) { spec.books.map { Animatable(0f) } }

    LaunchedEffect(Unit) {
        scroll.animateTo(
            spec.endProgress,
            animationSpec = tween(spec.scrollDuration, easing = FastOutSlowInEasing),
        )
    }

    LaunchedEffect(phase) {
        if (phase != AnimationPhase.Spiral) return@LaunchedEffect
        spec.books.forEachIndexed { index, book ->
            launch {
                delay((book.slot ?: index) * SpiralStaggerMs)
                spiralProgress[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(SpiralDurationMs, easing = FastOutSlowInEasing),
                )
            }
        }
    }

    val progress = if (phase == AnimationPhase.Spiral) spec.endProgress else scroll.value

    spec.books.forEachIndexed { index, book ->
        val start = scrollPosition(
            index = index,
            progress = progress,
            stepPx = stepPx,
            loop = loop,
            bookHeightPx = bookHeightPx,
            xOffsetPx = xOffsetPx,
            direction = spec.direction,
        )
        val pos = bookPosition(book, start, spiralProgress[index].value, phase)

        Image(
            painter = painterResource(id = book.frontCover),
            contentDescription = book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset { IntOffset(pos.x.roundToInt(), pos.y.roundToInt()) }
                .size(BookWidth, BookHeight),
        )
    }
}
private fun scrollPosition(
    index: Int,
    progress: Float,
    stepPx: Float,
    loop: Float,
    bookHeightPx: Float,
    xOffsetPx: Float,
    direction: ScrollDirection,
): Offset {
    val rawY = (index * stepPx + progress * loop) % loop
    val y = if (direction == ScrollDirection.Down) rawY else loop - rawY
    return Offset(xOffsetPx, y - bookHeightPx)
}
private fun bookPosition(
    book: Book,
    start: Offset,
    t: Float,
    phase: AnimationPhase,
): Offset {
    val endX = book.spiralAnimationX
    val endY = book.spiralAnimationY
    if (phase != AnimationPhase.Spiral || endX == null || endY == null) return start

    val control1 = Offset(
        book.controlPointX ?: (start.x + 300f),
        book.controlPointY ?: (start.y - 400f),
    )
    val control2 = Offset(
        book.controlPoint2X ?: (start.x + 150f),
        book.controlPoint2Y ?: (start.y - 200f),
    )
    return cubicBezier(t, start, control1, control2, Offset(endX, endY))
}

private fun cubicBezier(t: Float, p0: Offset, p1: Offset, p2: Offset, p3: Offset): Offset {
    val u = 1f - t
    val a = u * u * u
    val b = 3f * u * u * t
    val c = 3f * u * t * t
    val d = t * t * t
    return Offset(
        a * p0.x + b * p1.x + c * p2.x + d * p3.x,
        a * p0.y + b * p1.y + c * p2.y + d * p3.y,
    )
}