package com.zeyad.tiktokdownloader.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.SocketTimeoutException

//TODO: as there is a shorten url that can't be valid this class logic to expand it by making a http header and expand it
class TikTokRepository {
    private val expandedUrlLiveData = MutableLiveData<String>()
    fun getExpandedUrlLiveData(): LiveData<String> {
        return expandedUrlLiveData
    }

    suspend fun expandShortenedUrl(shortenedUrl: String) = withContext(Dispatchers.IO) {
        try {
            val client = OkHttpClient()
            val request = Request.Builder()
                .url(shortenedUrl)
                .build()

            val response = client.newCall(request).execute()
            val expandedUrl = response.request().url().toString()
            expandedUrlLiveData.postValue(expandedUrl)
        } catch (e: SocketTimeoutException) {
            expandedUrlLiveData.postValue("Timeout occurred: ${e.message}")
        } catch (e: Exception) {
            expandedUrlLiveData.postValue("Error: ${e.message}")
        }
    }
}