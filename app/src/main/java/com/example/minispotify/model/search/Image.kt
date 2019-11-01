package com.example.minispotify.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(
    val height: Int,
    val url: String,
    val width: Int
): Parcelable