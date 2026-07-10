package com.example.ebook.model

import androidx.annotation.DrawableRes

data class Book(
    val title: String,
    @DrawableRes val frontCover: Int,
    @DrawableRes val backCover: Int? = null,
    @DrawableRes val spineCover: Int? = null,
)
data class BookCovers(
    @DrawableRes val front: Int,
    @DrawableRes val back: Int,
    @DrawableRes val spine: Int,
)