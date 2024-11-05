package com.zeyad.tiktokdownloader.data.models

import android.net.Uri

data class MediaModel(
    val name: String,
    val uri: Uri,
    val duration: String,
    val size: String
) {
}