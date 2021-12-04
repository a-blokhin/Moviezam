package com.example.moviezam.models

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type {
    FILM, ARTIST, SONG, NO
}
fun getType(type: Type) = when(type){
    Type.FILM -> "film"
    Type.ARTIST -> "artist"
    Type.SONG -> "song"
    else -> "no"
}


@Entity
class FavouriteEntity(var id: Long, var type: String) {
    @PrimaryKey(autoGenerate = true) var i: Long = 0
}