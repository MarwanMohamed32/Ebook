package com.example.ebook

data class Book(
    val title: String,
    val frontCover: Int,
    val backCover: Int?,
    val spineCover: Int?,
    val slot: Int? = null,
    val spiralAnimationX : Float?,
    val spiralAnimationY : Float?,
    val controlPointX : Float?,
    val controlPointY : Float?,
    val controlPoint2X : Float?,
    val controlPoint2Y : Float?
)
