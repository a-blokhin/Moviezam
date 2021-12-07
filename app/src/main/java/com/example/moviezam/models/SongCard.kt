package com.example.moviezam.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "song_table")
data class SongCard(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "artist")
    @SerializedName("artist")
    val artist: String,
    @ColumnInfo(name = "album_name")
    @SerializedName("album_name")
    val album: String,
    @ColumnInfo(name = "external_art_url")
    @SerializedName("external_art_url")
    val image: String,
    @ColumnInfo(name = "description")
    @SerializedName("description")
    val description: String
)