package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class ArtistCard(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("external_art_url")
    val image: String
)