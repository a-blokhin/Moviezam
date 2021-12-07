package com.example.moviezam

import android.app.Application
import com.example.moviezam.models.SearchHistoryDatabase
import com.example.moviezam.repository.SearchRepository

class App: Application() {
    val database by lazy { SearchHistoryDatabase.getInstance(this) }
    val searchRepo by lazy { database?.searchDao?.let { SearchRepository(it) } }
}