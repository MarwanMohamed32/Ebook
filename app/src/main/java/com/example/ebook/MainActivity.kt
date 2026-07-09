package com.example.ebook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ebook.ui.theme.EbookTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EbookTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = Color.White
                ) { innerPadding ->
                    var spiralTriggered by remember { mutableStateOf(false) }

                    val bookProgress by rememberInfiniteTransition(label = "bookPath")
                        .animateFloat(
                            initialValue = 0f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(
                                    durationMillis = 3000,
                                    easing = FastOutSlowInEasing,
                                ),
                                repeatMode = RepeatMode.Reverse,
                            ),
                            label = "bookProgress",
                        )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        BookSpiralScreen(
                            modifier = Modifier,
                            spiralTriggered = spiralTriggered,
                        )
                        BookScene {
                            Book3D(
                                R.drawable.front_cover,
                                R.drawable.back_cover,
                                R.drawable.spine_cover,
                                progress = bookProgress,
                            )
                        }
                        SplashBottomSheet(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            onContinue = { spiralTriggered = true },
                        )
                    }

                }
            }
        }
    }
}