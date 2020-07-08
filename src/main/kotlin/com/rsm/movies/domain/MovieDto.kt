package com.rsm.movies.domain

import Movie

class MovieDto(movie: Movie) {

    var title: String? = ""
    var release_year: String? = ""
    var poster_path: String? = ""
    var vote_average: Double? = null
    var overview: String? = ""

    init {
        this.title = movie.title
        this.release_year = movie.release_date.substring(0).split("-")[0]
        this.poster_path = movie.poster_path
        this.vote_average = movie.vote_average
        this.overview = movie.overview
    }
}
