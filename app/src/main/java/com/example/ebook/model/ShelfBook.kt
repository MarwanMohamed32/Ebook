package com.example.ebook.model
data class ShelfBook(
    val book: Book,
    val trajectory: SpiralTrajectory? = null,
    val slot: Int? = null,
)