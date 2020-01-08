package com.rsm.movies.domain

import java.time.LocalDate

class MovieDto() {
    var title: String = ""
    var releaseDate: LocalDate = LocalDate.of(1900, 1, 1)
    var posterPath: String = ""

    constructor(title: String, releaseDate: LocalDate, posterPath: String) : this() {
        this.title = title
        this.releaseDate = releaseDate
        this.posterPath = posterPath
    }
}
