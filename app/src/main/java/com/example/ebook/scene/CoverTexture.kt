package com.example.ebook.scene

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.filament.Engine
import com.google.android.filament.Texture
import io.github.sceneview.texture.ImageTexture

/**
 * Loads [resId] as a Filament texture, rotated by [rotationDegrees] and optionally mirrored, so it
 * lands the right way up on the book model's UV layout.
 *
 * The texture is destroyed with the engine when this leaves composition.
 */
@Composable
internal fun rememberCoverTexture(
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