package com.example.incrediblemovieinfoapp.data.model

import android.net.Uri
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

data class Movie (
    var movieTitle: String = "",
    var moviePlot: String = "",
    var movieLength : Duration  = 0.toDuration(DurationUnit.SECONDS),
    var movieYear : Int = 1900,
    var movieRate : Float = 0f,
    var movieGenres: List<String> = listOf(),
    var movieImageUri : Uri? = null) {
}