package com.zeyad.tiktokdownloader.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zeyad.tiktokdownloader.R
import com.zeyad.tiktokdownloader.data.models.MediaModel
import com.zeyad.tiktokdownloader.databinding.VideoItemsBinding
import com.zeyad.tiktokdownloader.util.Utils

class MediaAdapter : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {
    private var mediaList: ArrayList<MediaModel> = ArrayList()
    private var isVideo: Boolean = true

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = VideoItemsBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.video_items, parent, false
        )
        return MediaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mediaList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = mediaList[position]
        holder.binding.apply {
            Glide.with(root.context).load(mediaItem.uri)
                .placeholder(R.drawable.music)
                .into(tvImgMedia)
            Log.d("MainActivity: uri is ", mediaItem.uri.toString())
            tvVideoName.text = mediaItem.name
            tvVideoName.isSelected = true
            tvVideoDetails.text = (mediaItem.size + " - " + mediaItem.duration)
            imgMore.setOnClickListener {
                showPopUpMenu(imgMore, root.context, mediaItem.uri)
            }
            root.setOnClickListener {
                // show media
                if (isVideo) {
                    Utils.showVideo(
                        uri = mediaItem.uri,
                        root.context
                    )
                } else {
                    Utils.showAudio(
                        uri = mediaItem.uri,
                        root.context
                    )
                }

            }

        }
    }

    private fun showPopUpMenu(view: View, context: Context, uri: Uri) {
        val popupMenu = PopupMenu(context, view, Gravity.START)
        popupMenu.menuInflater.inflate(R.menu.menu_more, popupMenu.menu)
        isVideo = Utils.isVideoFile(context, uri)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    if (isVideo) {
                        Utils.shareVideo(context, uri)
                    } else {
                        Utils.shareAudio(context, uri)
                    }
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: ArrayList<MediaModel>) {
        mediaList = newList
        notifyDataSetChanged()
    }
}