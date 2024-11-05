package com.zeyad.tiktokdownloader.util

object LinkValidator {

    fun isValidTikTokLink(link: String): Boolean {
        val regex = "https?://(www\\.)?(tiktok\\.com|vm\\.tiktok\\.com)/[\\w\\-]+/?".toRegex()
        return link.matches(regex)
    }
}