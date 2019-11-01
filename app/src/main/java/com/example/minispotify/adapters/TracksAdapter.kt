package com.example.minispotify.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.minispotify.R
import com.example.minispotify.model.search.Item
import kotlinx.android.synthetic.main.track_item.view.*

/**
 * this adapter is for track list in search screen
 * adapter Model is Item and we show cover of the
 * track and couple of data
 * i used diffutil to optimize recyclerview performance
 */
class TracksAdapter(private val interaction: Interaction? = null) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {



    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {

            //id is the property that is unit and i
            //use it to consider two Items the same
            return  oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {

            //name and album name are the properties
            // thar i consider define two Item contents the same
            return oldItem.name == newItem.name && oldItem.album.name == newItem.album.name
        }

    }
    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return TracksViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.track_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //inject current Item to viewholder
        when (holder) {
            is TracksViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<Item>) {
        differ.submitList(list)
    }

    class TracksViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Item) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }

            //set recyclerView data to views
            trackName.text = item.name
            artistName.text = item.artists.get(0).name
            albumName.text = item.album.name
            musicType.text = item.popularity.toString()

            //i set a place holder for track cover imageView
            Glide.with(context)
                .load(item.album.images.get(0).url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }

                })
                .placeholder(  R.drawable.ic_compact_disc )
                .into(trackImage)
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Item)
    }
}

