package com.example.ebook.model

import androidx.compose.ui.geometry.Offset
data class SpiralTrajectory(
    val destination: Offset,
    val control1: Offset,
    val control2: Offset,
) {
    fun positionAt(t: Float, start: Offset): Offset {
        val u = 1f - t
        val a = u * u * u
        val b = 3f * u * u * t
        val c = 3f * u * t * t
        val d = t * t * t
        return Offset(
            a * start.x + b * control1.x + c * control2.x + d * destination.x,
            a * start.y + b * control1.y + c * control2.y + d * destination.y,
        )
    }
}