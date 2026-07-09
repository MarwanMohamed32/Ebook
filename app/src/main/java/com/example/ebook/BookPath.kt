package com.example.ebook

import dev.romainguy.kotlin.math.Float3

/**
 * Describes how a book moves along its showcase path.
 *
 * Progress runs `0f → 1f` and the book slides straight along X, from [startX] at `t = 0` to [endX]
 * at `t = 1`. No curve, no turning.
 *
 * Both ends are in the book's local units (the model is scaled to ~1.5 units), relative to the
 * book's base position. Negative is left, positive is right.
 */
data class BookPath(
    val startX: Float = -0.2f,
    val endX: Float = 0.2f,
) {
    fun positionAt(t: Float): Float3 = Float3(x = startX + (endX - startX) * t)
}