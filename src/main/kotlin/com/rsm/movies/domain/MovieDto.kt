package com.rsm.movies.domain

import java.time.LocalDate

class MovieDto() {
    var originalTitle: String? = null
    var releaseDate: LocalDate? = null
    var posterPath: String? = null

    constructor(originalTitle: String, releaseDate: LocalDate, posterPath: String) : this() {
        this.originalTitle = originalTitle
        this.releaseDate = releaseDate
        this.posterPath = posterPath
    }
}


