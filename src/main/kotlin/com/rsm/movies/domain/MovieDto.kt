package com.rsm.movies.domain

import java.time.LocalDate

class MovieDto() {
    var originalTitle: String = ""
    var releaseDate: LocalDate = LocalDate.of(1900, 1, 1)
    var posterPath: String = ""

    constructor(originalTitle: String, releaseDate: LocalDate, posterPath: String) : this() {
        this.originalTitle = originalTitle
        this.releaseDate = releaseDate
        this.posterPath = posterPath
    }
}
