package com.example.moviezam.models
import com.google.gson.annotations.SerializedName

data class Favourite(
    @SerializedName("favourites")
    val favourites: List<FavouriteEntity>
)