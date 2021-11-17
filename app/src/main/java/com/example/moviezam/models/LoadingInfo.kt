package com.example.moviezam.models

data class LoadingInfo(var currPageNumber: Int = 1, var currSearchText : String = "",
                       var hasPagesToLoad: Boolean = true)