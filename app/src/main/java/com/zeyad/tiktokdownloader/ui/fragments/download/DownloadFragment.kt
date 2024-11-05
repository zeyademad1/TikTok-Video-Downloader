package com.zeyad.tiktokdownloader.ui.fragments.download

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.zeyad.tiktokdownloader.R
import com.zeyad.tiktokdownloader.databinding.FragmentDownloadBinding
import com.zeyad.tiktokdownloader.util.Utils
import com.zeyad.tiktokdownloader.util.Utils.TIKTOK_DOWNLOAD_PATH
import com.zeyad.tiktokdownloader.util.showToast
import kotlinx.coroutines.launch

class DownloadFragment : Fragment() {
    private val viewModel: DownloadViewModel by activityViewModels()
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!
    private var videoId: String? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(layoutInflater)

        binding.btnDownload.setOnClickListener {
            hideKeyboard(binding.root)
            startReadExternalStoragePermission()
        }



        binding.clearText.setOnClickListener {
            binding.urlInput.setText("")
        }


        binding.btnPaste.setOnClickListener {
            val copyBoardText = Utils.pasteFromClipboard(requireContext())
            if (copyBoardText.isNotEmpty()) {
                binding.urlInput.setText(copyBoardText)
            } else {
                binding.urlInput.setText("")
            }
        }


        binding.cdAudio.setOnClickListener {
            viewModel.downloadAudio(
                "Video-$videoId.mp3",
                "${Utils.DOWNLOAD_AUDIO_LINK}$videoId.mp3",
                requireContext()
            )
        }

        binding.cdOriginalVideo.setOnClickListener {
            viewModel.downloadVideo(
                "Video-Original-$videoId.mp4",
                "${Utils.DOWNLOAD_ORIGINAL_VIDEO_LINK}$videoId.mp4",
                requireContext()
            )
        }


        binding.cdDownloadHD.setOnClickListener {
            viewModel.downloadVideo(
                "Video-WMHD-$videoId.mp4",
                "${Utils.DOWNLOAD_VIDEO_WITHOUT_WATERMARK_HD}$videoId.mp4",
                requireContext()
            )
        }


        binding.downloadAnother.setOnClickListener {
            binding.cdDownloadOptions.visibility = View.GONE
        }



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//private fun startReadExternalStoragePermission() {
//    if(Build.VERSION.SDK_INT>Build.VERSION_CODES.R){
//        requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
//    }else{
//        requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//    }
//}
//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
//private fun startReadExternalStoragePermission() {
//    when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
//            // Android 13+ - use READ_MEDIA_VIDEO permission
//            requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
//        }
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
//            // Android 11 and 12 - use READ_EXTERNAL_STORAGE
//            requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//        else -> {
//            // For Android 10 and below - continue as normal
//            requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//        }
//    }
//}

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//private fun startReadExternalStoragePermission() {
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//        // For Android 13+, request specific media permissions for video
//        requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
//    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
//        // For Android 11 and 12, broad access might be needed
//        if (Environment.isExternalStorageManager()) {
//            performDownload()
//        } else {
//            // Request user to enable MANAGE_EXTERNAL_STORAGE through settings
//            val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
//                data = Uri.parse("package:${requireContext().packageName}")
//            }
//            startActivity(intent)
//        }
//    } else {
//        // For Android 10 and below, use READ_EXTERNAL_STORAGE
//        requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//    }
//}
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private fun startReadExternalStoragePermission() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_MEDIA_VIDEO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            performDownload()
        } else {
            requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_MEDIA_VIDEO)
        }
    } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.R) {
        if (Environment.isExternalStorageManager()) {
            performDownload()
        } else {
            val intent = Intent(ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                data = Uri.parse("package:${requireContext().packageName}")
            }
            startActivity(intent)
        }
    } else {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startWriteExternalStoragePermission()
        } else {
            requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}


    private val requestReadExternalPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                startWriteExternalStoragePermission()
            } else {
                performDownload()
            }

        } else {
            viewModel.showAlertDialog(
                getString(R.string.media_permission),
                getString(R.string.please_allow_media_permission_to_continue),
                requireContext()
            )
        }
    }

    private fun startWriteExternalStoragePermission() {
        requestWriteExternalPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private val requestWriteExternalPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            performDownload()
        } else {

            viewModel.showAlertDialog(
                getString(R.string.media_permission),
                getString(R.string.please_allow_media_permission_to_continue),
                requireContext()

            )
        }
    }


    private fun hideKeyboard(view: View) {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun performDownload() {
        if (binding.urlInput.text.toString().isNotEmpty()) {
            lifecycleScope.launch {
                if (Utils.isValidUrl(binding.urlInput.text.toString())) {

                    val dialogBuilder = AlertDialog.Builder(context)
                    val inflater = requireActivity().layoutInflater
                    val dialogView: View =
                        inflater.inflate(R.layout.fetching_dialog, null, false)
                    dialogBuilder.setView(dialogView)
                    val alertDialog = dialogBuilder.create()
                    alertDialog.setCancelable(false)



                    alertDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    alertDialog.window!!.attributes.windowAnimations = R.style.DialogAnimation



                    alertDialog.show()
                    val window: Window? = alertDialog.window
                    window!!.setLayout(
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                    )



                    viewModel.expandShortenedUrl(binding.urlInput.text.toString())
                    viewModel.getExpandedUrlLiveData()
                        .observe(viewLifecycleOwner) { expandedUrl ->
                            alertDialog.dismiss()
                            if (expandedUrl == "Error:" || expandedUrl == "Timeout occurred:") {
                                showToast(expandedUrl.toString())
                                return@observe
                            } else {
                                videoId = viewModel.extractVideoIdFromUrl(expandedUrl)
                                if (!TIKTOK_DOWNLOAD_PATH.exists()) {
                                    TIKTOK_DOWNLOAD_PATH.mkdir()
                                }
                                Glide.with(requireContext())
                                    .load("${Utils.SOURCE_IMAGEVIEW}$videoId.webp")
                                    .into(binding.srcImage)

                                binding.urlInput.setText("")
                                binding.cdDownloadOptions.visibility = View.VISIBLE
                            }
                        }

                } else {
                    showToast("Invalid url, please check again that pasted url is of tiktok video.")
                }
            }
        } else {
            showToast("url is not valid!")
        }
    }

}