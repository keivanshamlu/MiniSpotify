package com.example.minispotify.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ExternalUrls(
    val spotify: String
): Parcelable