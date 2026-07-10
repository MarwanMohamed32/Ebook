package com.example.ebook.splash

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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.ebook.model.ShelfBook
import com.example.ebook.ui.theme.SplashSurface
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

private val CoverWidth = 90.dp
private val CoverHeight = 140.dp
private val CoverGap = 12.dp
private val ColumnGap = 20.dp

private const val ScrollDurationMs = 2400
private const val SpiralDurationMs = 1350
private const val SpiralStaggerMs = 70L

private const val TiltDegrees = -15f
private const val ContentShiftX = 150f

private val TiltScale = Math.toRadians(TiltDegrees.toDouble())
    .let { abs(cos(it)) + abs(sin(it)) }
    .toFloat()

private enum class ScrollDirection { Up, Down }
private data class ColumnScroll(
    val direction: ScrollDirection,
    val startProgress: Float,
    val endProgress: Float,
)

private val ColumnScrolls = listOf(
    ColumnScroll(ScrollDirection.Down, startProgress = 0.55f, endProgress = 0.6f),
    ColumnScroll(ScrollDirection.Up, startProgress = 0.4f, endProgress = 0.8f),
    ColumnScroll(ScrollDirection.Down, startProgress = 0.4f, endProgress = 0.8f),
    ColumnScroll(ScrollDirection.Up, startProgress = 0.4f, endProgress = 0.8f),
)
@Composable
fun BookShelf(
    columns: List<List<ShelfBook>>,
    phase: SplashPhase,
    modifier: Modifier = Modifier,
) {
    val columnStep = CoverWidth + ColumnGap

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SplashSurface)
            .clipToBounds(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationZ = TiltDegrees
                    scaleX = TiltScale
                    scaleY = TiltScale
                    translationX = ContentShiftX
                },
        ) {
            columns.forEachIndexed { index, books ->
                BookColumn(
                    books = books,
                    xOffset = columnStep * index,
                    scroll = ColumnScrolls[index % ColumnScrolls.size],
                    phase = phase,
                )
            }
        }
    }
}

@Composable
private fun BookColumn(
    books: List<ShelfBook>,
    xOffset: Dp,
    scroll: ColumnScroll,
    phase: SplashPhase,
) {
    val density = LocalDensity.current
    val metrics = remember(density, xOffset, books.size) {
        with(density) {
            ColumnMetrics(
                xOffsetPx = xOffset.toPx(),
                stepPx = (CoverHeight + CoverGap).toPx(),
                coverHeightPx = CoverHeight.toPx(),
                coverCount = books.size,
            )
        }
    }

    val scrollProgress = remember { Animatable(scroll.startProgress) }
    val spiralProgress = remember(books) { books.map { Animatable(0f) } }

    LaunchedEffect(Unit) {
        scrollProgress.animateTo(
            targetValue = scroll.endProgress,
            animationSpec = tween(ScrollDurationMs, easing = FastOutSlowInEasing),
        )
    }

    LaunchedEffect(phase) {
        if (phase != SplashPhase.Spiral) return@LaunchedEffect
        books.forEachIndexed { index, shelfBook ->
            launch {
                delay((shelfBook.slot ?: index) * SpiralStaggerMs)
                spiralProgress[index].animateTo(
                    targetValue = 1f,
                    animationSpec = tween(SpiralDurationMs, easing = FastOutSlowInEasing),
                )
            }
        }
    }

    val progress = if (phase == SplashPhase.Spiral) scroll.endProgress else scrollProgress.value

    books.forEachIndexed { index, shelfBook ->
        val onShelf = metrics.positionOf(index, progress, scroll.direction)
        val position = if (phase == SplashPhase.Spiral) {
            shelfBook.trajectory?.positionAt(spiralProgress[index].value, onShelf) ?: onShelf
        } else {
            onShelf
        }

        Image(
            painter = painterResource(shelfBook.book.frontCover),
            contentDescription = shelfBook.book.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .offset { IntOffset(position.x.roundToInt(), position.y.roundToInt()) }
                .size(CoverWidth, CoverHeight),
        )
    }
}

private data class ColumnMetrics(
    val xOffsetPx: Float,
    val stepPx: Float,
    val coverHeightPx: Float,
    val coverCount: Int,
) {
    private val loopPx = stepPx * coverCount
    fun positionOf(index: Int, progress: Float, direction: ScrollDirection): Offset {
        val rawY = (index * stepPx + progress * loopPx) % loopPx
        val y = if (direction == ScrollDirection.Down) rawY else loopPx - rawY
        return Offset(xOffsetPx, y - coverHeightPx)
    }
}