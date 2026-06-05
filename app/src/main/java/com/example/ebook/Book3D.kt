package com.example.ebook

import android.graphics.Matrix
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.android.filament.View
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.SceneView
import io.github.sceneview.SurfaceType
import io.github.sceneview.createView
import io.github.sceneview.material.setBaseColorMap
import io.github.sceneview.node.ModelNode
import io.github.sceneview.rememberEngine
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.rememberModelLoader
import io.github.sceneview.rememberView
import io.github.sceneview.texture.ImageTexture

@Composable
fun Book3D(
    @DrawableRes frontCover: Int,
    @DrawableRes backCover: Int,
    @DrawableRes spine: Int,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val engine = rememberEngine()
    val view = rememberView(engine) {
        createView(engine).apply {
            dithering = View.Dithering.NONE
            ambientOcclusionOptions = ambientOcclusionOptions.apply { enabled = false }
        }
    }
    val modelLoader = rememberModelLoader(engine)
    val model = rememberModelInstance(modelLoader, "book.glb")

    val frontCoverTexture = remember(engine, frontCover) {
        val frontCoverBitmap = ImageTexture.getBitmap(context, frontCover)
        val rotatedFrontCoverBitmap = android.graphics.Bitmap.createBitmap(
            frontCoverBitmap,
            0,
            0,
            frontCoverBitmap.width,
            frontCoverBitmap.height,
            Matrix().apply { postRotate(90.0f) },
            true
        )

        ImageTexture.Builder()
            .bitmap(rotatedFrontCoverBitmap)
            .build(engine)
    }

    val backCoverTexture = remember(engine, backCover) {
        val backCoverBitmap = ImageTexture.getBitmap(context, backCover)
        val rotatedBackCoverBitmap = android.graphics.Bitmap.createBitmap(
            backCoverBitmap,
            0,
            0,
            backCoverBitmap.width,
            backCoverBitmap.height,
            Matrix().apply {
                postRotate(-90.0f)
                postScale(1.0f, -1.0f)
            },
            true
        )
        ImageTexture.Builder()
            .bitmap(rotatedBackCoverBitmap)
            .build(engine)
    }

    val spineCoverTexture = remember(engine, spine) {
        val spineCoverBitmap = ImageTexture.getBitmap(context, spine)
        val rotatedSpineCoverBitmap = android.graphics.Bitmap.createBitmap(
            spineCoverBitmap,
            0,
            0,
            spineCoverBitmap.width,
            spineCoverBitmap.height,
            Matrix().apply {
                postRotate(90.0f)
                postScale(1.0f, -1.0f)
            },
            true
        )
        ImageTexture.Builder()
            .bitmap(rotatedSpineCoverBitmap)
            .build(engine)
    }

    DisposableEffect(frontCoverTexture, backCoverTexture, spineCoverTexture) {
        onDispose {
            engine.destroyTexture(frontCoverTexture)
            engine.destroyTexture(backCoverTexture)
            engine.destroyTexture(spineCoverTexture)
        }
    }

    LaunchedEffect(model, frontCoverTexture, backCoverTexture, spineCoverTexture) {
        model?.materialInstances
            ?.firstOrNull { it.name == "front_cover" }
            ?.setBaseColorMap(frontCoverTexture)

        model?.materialInstances
            ?.firstOrNull { it.name == "back_cover" }
            ?.setBaseColorMap(backCoverTexture)

        model?.materialInstances
            ?.firstOrNull { it.name == "spine" }
            ?.setBaseColorMap(spineCoverTexture)
    }

    Box(modifier = modifier.fillMaxSize()) {
        SceneView(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            surfaceType = SurfaceType.TextureSurface,
            isOpaque = false,
            engine = engine,
            view = view,
            modelLoader = modelLoader,
            cameraManipulator = null,
            onGestureListener = null,
            onTouchEvent = { _, _ -> true }
        ) {
            model?.let {
                ModelNode(
                    modelInstance = it,
                    rotation = Float3(x = -180.0f, y = 0.0f, z = 90.0f),
                    scaleToUnits = 1.5f,
                    autoAnimate = true
                )
            }
        }
    }
}


