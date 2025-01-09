package com.example.incrediblemovieinfoapp.data.models

import androidx.room.TypeConverter

class GenreListConverter {
    @TypeConverter
    fun fromGenreList(value: List<Pair<Int, String>>?): String {
        return value?.joinToString(",") { "${it.first}:${it.second}" } ?: ""
    }

    @TypeConverter
    fun toGenreList(value: String?): List<Pair<Int, String>> {
        return value?.split(",")?.map {
            val (id, label) = it.split(":")
            id.toInt() to label
        } ?: emptyList()
    }
}
