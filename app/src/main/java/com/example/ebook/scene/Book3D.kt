package com.example.ebook.scene

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.ebook.model.BookCovers
import dev.romainguy.kotlin.math.Float3
import io.github.sceneview.SceneScope
import io.github.sceneview.material.setBaseColorMap
import io.github.sceneview.rememberModelInstance

private const val ModelAsset = "book.glb"
private const val ScaleToUnits = 1.3f
private val ModelRotation = Float3(x = -180.0f, y = 0.0f, z = 90.0f)
private const val FrontCoverMaterial = "front_cover"
private const val BackCoverMaterial = "back_cover"
private const val SpineMaterial = "spine"

@Composable
fun SceneScope.Book3D(
    modifier: Modifier = Modifier,
    covers: BookCovers,
    position: Float3 = Float3(0f),
    progress: Float = 0.5f,
    path: BookPath = BookPath(),
) {
    val frontTexture = rememberCoverTexture(engine, covers.front, rotationDegrees = 90f)
    val backTexture =
        rememberCoverTexture(engine, covers.back, rotationDegrees = -90f, flipVertical = true)
    val spineTexture =
        rememberCoverTexture(engine, covers.spine, rotationDegrees = 90f, flipVertical = true)

    val model = rememberModelInstance(modelLoader, ModelAsset)

    LaunchedEffect(model, frontTexture, backTexture, spineTexture) {
        model ?: return@LaunchedEffect
        model.materialInstances.firstOrNull { it.name == FrontCoverMaterial }
            ?.setBaseColorMap(frontTexture)
        model.materialInstances.firstOrNull { it.name == BackCoverMaterial }
            ?.setBaseColorMap(backTexture)
        model.materialInstances.firstOrNull { it.name == SpineMaterial }
            ?.setBaseColorMap(spineTexture)
    }

    model?.let {
        Node(position = position + path.positionAt(progress)) {
            ModelNode(
                modelInstance = it,
                centerOrigin = Float3(0f),
                rotation = ModelRotation,
                scaleToUnits = ScaleToUnits,
                autoAnimate = true,
            )
        }
    }
}
