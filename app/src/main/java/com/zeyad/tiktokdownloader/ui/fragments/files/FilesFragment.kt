package com.zeyad.tiktokdownloader.ui.fragments.files

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zeyad.tiktokdownloader.R
import com.zeyad.tiktokdownloader.adapters.FoldersAdapters
import com.zeyad.tiktokdownloader.databinding.FragmentFilesBinding
import com.zeyad.tiktokdownloader.ui.fragments.download.DownloadViewModel
import kotlinx.coroutines.launch

class FilesFragment : Fragment() {
    private var _binding: FragmentFilesBinding? = null
    private val binding get() = _binding!!
    private val downloadViewModel: DownloadViewModel by activityViewModels()
    private lateinit var adapter: FoldersAdapters


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilesBinding.inflate(inflater, container, false)
        setupRv()

        lifecycleScope.launch {
            val foldersList = downloadViewModel.getFolderInfo()
            if (foldersList.isNotEmpty()) {
                binding.tvNotFound.visibility = View.GONE
                adapter.submitList(foldersList)
                Log.d("MainActivity: folderList", foldersList.toString())
            }
        }
        // handle folder click
        adapter.onFolderClick = { folderModel ->
            val bundle = Bundle()
            try {
                when (folderModel.name) {
                    "AUDIOS" -> {
                        bundle.putString("MEDIA", "audio")
                        findNavController().navigate(
                            R.id.action_navigation_files_to_mediaFragment,
                            bundle
                        )
                    }

                    "VIDEOS" -> {
                        bundle.putString("MEDIA", "videos")
                        findNavController().navigate(
                            R.id.action_navigation_files_to_mediaFragment,
                            bundle
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }



        return binding.root
    }

    private fun setupRv() {
        adapter = FoldersAdapters()
        binding.filesList.setHasFixedSize(true)
        binding.filesList.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}