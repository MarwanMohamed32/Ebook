package com.example.ebook.splash

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.ebook.data.BookCatalog
import com.example.ebook.scene.Book3D
import com.example.ebook.scene.BookPath
import com.example.ebook.scene.BookScene

private const val BookDriftDurationMs = 1000
private const val BookDriftDelayMs = 500
private val FeaturedBookPath = BookPath(startX = -0.8f, endX = 0.8f)
private const val BookSpacing = -0.45f

@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    var phase by remember { mutableStateOf(SplashPhase.Scrolling) }
    val bookProgress by rememberBookDrift(phase)

    Box(modifier = modifier.fillMaxSize()) {
        BookShelf(columns = BookCatalog.columns, phase = phase)
        BookScene {
            if (phase == SplashPhase.Spiral) {
                for (i in 0 until 5) {
                    val j = 5 - i
                    val offsetStartX = BookSpacing * i
                    val offsetEndX = BookSpacing * j
                    Book3D(
                        covers = BookCatalog.fullCoverBooks[i],
                        path = BookPath(
                            startX = FeaturedBookPath.startX + offsetStartX,
                            endX = FeaturedBookPath.endX + offsetEndX * -1,
                        ),
                        progress = bookProgress,
                    )
                }
            }
        }
        SplashBottomSection(
            onContinue = { phase = SplashPhase.Spiral },
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun rememberBookDrift(phase: SplashPhase): State<Float> =
    animateFloatAsState(
        targetValue = if (phase == SplashPhase.Spiral) 1f else 0f,
        animationSpec = tween(
            durationMillis = BookDriftDurationMs,
            delayMillis = BookDriftDelayMs,
            easing = LinearEasing,
        ),
        label = "bookProgress",
    )