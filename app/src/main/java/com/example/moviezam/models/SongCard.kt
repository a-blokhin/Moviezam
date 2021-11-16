package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class SongCard(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("artist")
    val artist: String,
    @SerializedName("album")
    val album: String,
    @SerializedName("external_art_url")
    val image: String
)