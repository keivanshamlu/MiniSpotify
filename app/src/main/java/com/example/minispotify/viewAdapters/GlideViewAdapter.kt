package com.example.minispotify.viewAdapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.minispotify.R

/**
 * to implement logic of loading a track in the layout
 */
object GlideViewAdapter {

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun ImageView.setImageUrl( imageUrl: String) {

            var context = this.context
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.ic_compact_disc)
                .into(this)
        }
}