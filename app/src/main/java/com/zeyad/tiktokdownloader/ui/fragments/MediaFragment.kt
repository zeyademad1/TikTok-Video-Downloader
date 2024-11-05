package com.zeyad.tiktokdownloader.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.zeyad.tiktokdownloader.adapters.MediaAdapter
import com.zeyad.tiktokdownloader.data.models.MediaModel
import com.zeyad.tiktokdownloader.databinding.FragmentMediaBinding
import com.zeyad.tiktokdownloader.ui.fragments.download.DownloadViewModel
import com.zeyad.tiktokdownloader.util.Utils
import kotlinx.coroutines.launch


class MediaFragment : Fragment() {
    private var _binding: FragmentMediaBinding? = null
    private val binding get() = _binding!!
    private lateinit var mediaAdapter: MediaAdapter
    private var mediaList: ArrayList<MediaModel> = ArrayList()
    private val downloadViewModel: DownloadViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMediaBinding.inflate(inflater, container, false)
        val argument: String? = requireArguments().getString("MEDIA")
        Log.d("MainActivity", arguments.toString())

        setupRv()
        lifecycleScope.launch {
            when (argument) {
                "audio" -> {
                    mediaList = downloadViewModel.getAudiosFromFolder(
                        requireContext(),
                        Utils.AUDIOS.absolutePath
                    )
                    if (mediaList.isNotEmpty()) {
                        mediaAdapter.submitList(mediaList)
                        binding.ivNoItems.visibility = View.GONE
                        binding.tvNoItems.visibility = View.GONE
                    } else {
                        binding.ivNoItems.visibility = View.VISIBLE
                        binding.tvNoItems.visibility = View.VISIBLE
                    }

                }

                "videos" -> {
                    mediaList = downloadViewModel.getVideosFromFolder(
                        requireContext(),
                        Utils.VIDEOS.absolutePath
                    )
                    if (mediaList.isNotEmpty()) {
                        mediaAdapter.submitList(mediaList)
                        binding.ivNoItems.visibility = View.GONE
                        binding.tvNoItems.visibility = View.GONE
                    } else {
                        binding.ivNoItems.visibility = View.VISIBLE
                        binding.tvNoItems.visibility = View.VISIBLE
                    }
                }
            }
        }
        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun setupRv() {
        mediaAdapter = MediaAdapter()
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = mediaAdapter
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}