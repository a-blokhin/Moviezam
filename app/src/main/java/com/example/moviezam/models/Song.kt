package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class Song (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_stub")
    val nameStub: String,
    @SerializedName("artist")
    val artist: String,
    @SerializedName("album_name")
    val albumName: String,
    @SerializedName("external_art_url")
    val externalArtUrl: String,
    @SerializedName("amazon")
    val amazon: String,
    @SerializedName("apple_music")
    val appleMusic: String,
    @SerializedName("itunes")
    val itunes: String,
    @SerializedName("spotify")
    val spotify: String,
    @SerializedName("youtube")
    val youtube: String,
    @SerializedName("films")
    val films: List<FilmCard>
)