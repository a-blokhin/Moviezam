package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class Film (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_stub")
    val nameStub: String,
    @SerializedName("air_date_start")
    val airDateStart: String,
    @SerializedName("air_date_end")
    val airDateEnd: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("artists")
    val artists: List<ArtistCard>,
    @SerializedName("songs")
    val songs: List<SongCard>,
    @SerializedName("similars")
    val similar: List<FilmCard>
)