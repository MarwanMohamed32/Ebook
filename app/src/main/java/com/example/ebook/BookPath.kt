package com.example.ebook

import dev.romainguy.kotlin.math.Float3
import kotlin.math.PI
import kotlin.math.sin

/**
 * Describes how a book moves and turns as it travels along its showcase path.
 *
 * Progress runs `0f → 1f`. The path curves *outward* — toward the viewer (`+Z`) — so the book
 * bulges out of the screen at the midpoint, and the book yaws about its vertical axis so that:
 *
 * - **start** (`t = 0`): a sliver of the **front** cover is visible,
 * - **middle** (`t = 0.5`): the **spine** faces the viewer head-on,
 * - **end** (`t = 1`): a sliver of the **back** cover is visible.
 *
 * Every distance is in the book's local units (the model is scaled to ~1.5 units). Tune the
 * fields to taste; e.g. raise [revealAngle] toward 90° for a full front-to-back sweep, or flip
 * its sign if the front and back covers come out swapped.
 */
data class BookPath(
    /** Sideways travel from start to end, along X. */
    val travelX: Float = 0.2f,
    /** How far the path bulges toward the viewer (+Z) at the midpoint — the "outward" curve. */
    val outwardBulge: Float = 0.5f,
    /** Vertical drift from start to end, along Y. */
    val riseY: Float = 0f,
    /** Yaw at the midpoint; 90° turns the spine square to the viewer. */
    val spineYaw: Float = 90f,
    /** How far the book rocks either side of [spineYaw]. Small = only a peek of each cover. */
    val revealAngle: Float = 25f,
) {
    /** Local-space position offset at progress [t], added to the book's base position. */
    fun positionAt(t: Float): Float3 = Float3(
        x = (t - 0.5f) * 2f * travelX,
        y = (t - 0.5f) * 2f * riseY,
        z = outwardBulge * sin(PI.toFloat() * t),
    )

    /** Yaw in degrees about the vertical axis at progress [t]: front → spine → back. */
    fun yawAt(t: Float): Float = spineYaw + (2f * t - 1f) * revealAngle
}