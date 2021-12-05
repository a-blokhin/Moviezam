package com.example.moviezam.models
import com.google.gson.annotations.SerializedName

data class Favourite(
    @SerializedName("songs")
    val favourites: List<FavouriteEntity>
)