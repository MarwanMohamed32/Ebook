package com.example.ebook

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.filament.Engine
import com.google.android.filament.Texture
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.SceneScope
import io.github.sceneview.material.setBaseColorMap
import io.github.sceneview.rememberModelInstance
import io.github.sceneview.texture.ImageTexture
/**
 * The actual 3D book.
 *
 * Loads `book.glb`, maps the given cover art onto its `front_cover`, `back_cover` and `spine`
 * materials, and adds the model to the surrounding [BookScene]. Call it inside a [BookScene]
 * content block.
 *
 * [progress] (`0f..1f`) animates the book along [path]: it arcs outward toward the viewer while
 * turning from the front cover, through the spine at the midpoint, to the back cover. [position]
 * offsets the whole path, so several books can share one scene.
 */
@Composable
fun SceneScope.Book3D(
    @DrawableRes frontCover: Int,
    @DrawableRes backCover: Int,
    @DrawableRes spine: Int,
    position: Float3 = Float3(0f),
    progress: Float = 0.5f,
    path: BookPath = BookPath(),
) {
    val frontCoverTexture = rememberCoverTexture(engine, frontCover, rotationDegrees = 90f)
    val backCoverTexture =
        rememberCoverTexture(engine, backCover, rotationDegrees = -90f, flipVertical = true)
    val spineCoverTexture =
        rememberCoverTexture(engine, spine, rotationDegrees = 90f, flipVertical = true)

    val model = rememberModelInstance(modelLoader, "book.glb")

    LaunchedEffect(model, frontCoverTexture, backCoverTexture, spineCoverTexture) {
        model ?: return@LaunchedEffect
        model.materialInstances.firstOrNull { it.name == "front_cover" }
            ?.setBaseColorMap(frontCoverTexture)
        model.materialInstances.firstOrNull { it.name == "back_cover" }
            ?.setBaseColorMap(backCoverTexture)
        model.materialInstances.firstOrNull { it.name == "spine" }
            ?.setBaseColorMap(spineCoverTexture)
    }

    model?.let {
        // The parent node carries the path: it translates along the outward curve and yaws about
        // the vertical axis (front → spine → back), while the child keeps the model's own upright
        // base orientation. Splitting them avoids folding the yaw into the (x, z) base Euler.
        Node(
            position = position + path.positionAt(progress),
            rotation = Float3(y = path.yawAt(progress)),
        ) {
            ModelNode(
                modelInstance = it,
                centerOrigin = Float3(0f),
                rotation = Float3(x = -180.0f, y = 0.0f, z = 90.0f),
                scaleToUnits = 1.5f,
                autoAnimate = true,
            )
        }
    }
}
@Composable
private fun rememberCoverTexture(
    engine: Engine,
    @DrawableRes resId: Int,
    rotationDegrees: Float,
    flipVertical: Boolean = false,
): Texture {
    val context = LocalContext.current
    val texture = remember(engine, resId, rotationDegrees, flipVertical) {
        val bitmap = ImageTexture.getBitmap(context, resId)
        val transformed = Bitmap.createBitmap(
            bitmap,
            0,
            0,
            bitmap.width,
            bitmap.height,
            Matrix().apply {
                postRotate(rotationDegrees)
                if (flipVertical) postScale(1.0f, -1.0f)
            },
            true,
        )
        ImageTexture.Builder()
            .bitmap(transformed)
            .build(engine)
    }
    DisposableEffect(texture) {
        onDispose { engine.destroyTexture(texture) }
    }
    return texture
}