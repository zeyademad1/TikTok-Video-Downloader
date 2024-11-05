package com.zeyad.tiktokdownloader

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.Settings
import androidx.annotation.RequiresApi

//@RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
//            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
//                data = Uri.parse("package:${requireContext().packageName}")
//            }
//            startActivity(intent)
//        }
//    } else {
//        // For Android 10 and below, use READ_EXTERNAL_STORAGE
//        requestReadExternalPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
//    }
//}