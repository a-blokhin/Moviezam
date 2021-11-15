package com.example.moviezam.viewmodels

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import com.example.moviezam.models.ShazamSearch

class ShazamViewModel {
    private val shazamSearch = ShazamSearch()
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun findSong(output: String, dir: String): String {
        return shazamSearch.search(output, dir)
    }
}