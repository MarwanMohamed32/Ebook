package com.example.ebook

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.android.filament.Renderer
import com.google.android.filament.View
import io.github.sceneview.SceneScope
import io.github.sceneview.SceneView
import io.github.sceneview.SurfaceType
import io.github.sceneview.createRenderer
import io.github.sceneview.createView
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberRenderer
import io.github.sceneview.rememberView

@Composable
fun BookScene(
    modifier: Modifier = Modifier,
    content: @Composable SceneScope.() -> Unit,
) {
    val engine = rememberEngine()
    val view = rememberView(engine) {
        createView(engine).apply {
            dithering = View.Dithering.NONE
            ambientOcclusionOptions = ambientOcclusionOptions.apply { enabled = false }
        }
    }
    val modelLoader = rememberModelLoader(engine)
    val renderer = rememberRenderer(engine) {
        createRenderer(engine).apply {
            clearOptions = Renderer.ClearOptions().apply {
                clear = true
                clearColor = doubleArrayOf(0.0, 0.0, 0.0, 0.0)
            }
        }
    }

    SceneView(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent),
        surfaceType = SurfaceType.TextureSurface,
        isOpaque = false,
        autoCenterContent = false,
        engine = engine,
        view = view,
        modelLoader = modelLoader,
        renderer = renderer,
        cameraManipulator = null,
        onGestureListener = null,
        onTouchEvent = { _, _ -> true },
        content = content,
    )
}