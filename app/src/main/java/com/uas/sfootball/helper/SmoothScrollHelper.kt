package com.uas.sfootball.helper

import android.content.Context

object SmoothScrollHelper {
    fun calculateOffset(context: Context, itemWidth: Int): Int {
        val screenWidth = context.resources.displayMetrics.widthPixels
        return (screenWidth / 2) - (itemWidth / 2)
    }
}