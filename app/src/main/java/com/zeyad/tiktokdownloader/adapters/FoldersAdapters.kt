package com.zeyad.tiktokdownloader.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zeyad.tiktokdownloader.R
import com.zeyad.tiktokdownloader.data.models.FolderModel
import com.zeyad.tiktokdownloader.databinding.ListFoldersBinding

class FoldersAdapters : RecyclerView.Adapter<FoldersAdapters.FolderViewHolder>() {

    private var foldersList: ArrayList<FolderModel> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_folders, parent, false)
        return FolderViewHolder(view)
    }

    override fun getItemCount(): Int {
        return foldersList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val item = foldersList[position]
        holder.binding.apply {
            tvFolderName.text = item.name
            cdMain.setOnClickListener {
                onFolderClick?.invoke(item)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newList: ArrayList<FolderModel>) {
        foldersList = newList
        notifyDataSetChanged()
    }

    var onFolderClick: ((FolderModel) -> Unit)? = null

    class FolderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ListFoldersBinding.bind(itemView)
    }
}
