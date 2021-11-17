package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class FilmCard(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("image")
    val image: String
)