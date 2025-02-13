package com.example.reelygoodmovies.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters


@Entity(tableName = "movies")
data class Movie(
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "plot")
    val plot: String,
    @ColumnInfo(name = "length")
    val length: Int,
    @ColumnInfo(name = "year")
    val year: Int,
    @ColumnInfo(name = "rate")
    val rate: Float,
    @TypeConverters(IntListConverter::class)
    @ColumnInfo(name = "genre")
    val genre: List<Int>,
    @ColumnInfo(name = "photo")
    val photo: String?,
    @ColumnInfo(name = "favorite")
    var favorite: Boolean,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


