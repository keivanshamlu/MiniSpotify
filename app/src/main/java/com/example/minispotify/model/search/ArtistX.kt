package com.example.minispotify.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ArtistX(
    val external_urls: ExternalUrlsXX,
    val href: String,
    val id: String,
    val name: String,
    val type: String,
    val uri: String
): Parcelable