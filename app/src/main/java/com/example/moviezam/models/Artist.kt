package com.example.moviezam.models

import com.google.gson.annotations.SerializedName

data class Artist (
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("name_stub")
    val nameStub: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("biography")
    val biography: String,
    @SerializedName("url_official")
    val urlOfficial: String,
    @SerializedName("url_wikipedia")
    val urlWikipedia: String,
    @SerializedName("url_youtube")
    val urlYoutube: String,
    @SerializedName("url_spotify")
    val urlSpotify: String,
    @SerializedName("url_applemusic")
    val urlAppleMusic: String,
    @SerializedName("url_itunes")
    val urlItunes: String,
    @SerializedName("url_amazon")
    val urlAmazon: String,
    @SerializedName("songs")
    val songs: List<SongCard>
)