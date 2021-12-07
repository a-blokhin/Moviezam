package com.example.moviezam.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "film_table")
data class FilmCard(
    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    val id: Int,
    @ColumnInfo(name = "name")
    @SerializedName("name")
    val name: String,
    @ColumnInfo(name = "resease_date")
    @SerializedName("release_date")
    val releaseDate: String,
    @ColumnInfo(name = "image")
    @SerializedName("image")
    val image: String,
    @ColumnInfo(name = "insert_time")
    var time: String
)