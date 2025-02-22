package com.example.reelygoodmovies.data.models

data class TrailerResponse(
    val results: List<Trailer> = emptyList() // אם אין תוצאות, תחזור רשימה ריקה
) {
    data class Trailer(
        val key: String?,
        val type: String
    ) {

    }
}


