package com.example.incrediblemovieinfoapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


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
    @ColumnInfo(name = "genre")
    val genre: String,
    @ColumnInfo(name = "photo")
    val photo: String?)

{
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}


