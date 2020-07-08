package com.rsm.movies.service

import com.rsm.movies.domain.MovieDto
import org.json.simple.JSONObject
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList

@Service
class TopRatedService : MoviesService() {

    override fun getMovie(): MovieDto? {
        while (true) {
            val movie = getTopRatedMovie(getMovieJson(getEndpointByTopRated(Random().nextInt(336))))
            if (movie != null) {
                return movie
            }
        }
    }

    private fun getEndpointByTopRated(page: Int): String {
        return "https://api.themoviedb.org/3/movie/top_rated?api_key=${API_KEY}&language=en-US&page=${page}"
    }

    private fun getTopRatedMovie(json: JSONObject?): MovieDto? {
        val attributes = json.toString().split("[{").toTypedArray()[1].split(",").toTypedArray()
        val movies = ArrayList<MovieDto>()
        var movie = MovieDto()
        var countTitle = 0
        var countDate = 0
        var countPoster = 0
        var countVoteAverage = 0
        var countOverview = 0
        var countBackdrop = 0

        for (attribute in attributes) {
            val splited = attribute.split(":").toTypedArray()

            if (countTitle == 0 && !attribute.contains("original_title") && attribute.contains("title")) {
                val splitedTitle = mutableListOf<String>()

                for (i in 1 until splited.size) {
                    splitedTitle += splited[i].substring(1).plus(" ").split("\"")
                }

                for (title in splitedTitle) {
                    movie.title += title
                }

                movie.title = movie.title?.trim()
                countTitle += 1
            }

            if (countDate == 0 && attribute.contains("release_date")) {
                val releaseYear = splited[1].substring(1).split("-")[0]
                movie.release_year = releaseYear
                countDate += 1
            }

            if (countPoster == 0 && attribute.contains("poster_path")) {
                val posterPath = splited[1].split(".")[0].split("/")[splited.size - 1].plus(".jpg")
                movie.poster_path = ("/$posterPath")
                countPoster += 1
            }

            if (countVoteAverage == 0 && attribute.contains("vote_average")) {
                movie.vote_average = splited[1]
                countVoteAverage += 1
            }

            if (countOverview == 0 && attribute.contains("overview")) {
                movie.overview = splited[1]
                countOverview += 1
            }

            if (countBackdrop == 0 && attribute.contains("backdrop_path")) {
                val backdropPath = splited[1].split(".")[0].split("/")[splited.size - 1].plus(".jpg")
                movie.backdrop_path = ("/$backdropPath")
                countBackdrop += 1
            }

            if (countTitle == 1 && countDate == 1 && countPoster == 1 && countVoteAverage == 1 && countOverview == 1 && countBackdrop == 1) {
                movies.add(movie)
                movie = MovieDto()
                countTitle = 0
                countDate = 0
                countPoster = 0
                countVoteAverage = 0
                countOverview = 0
                countBackdrop = 0
            }
        }

        return movies[Random().nextInt(movies.size)]
    }
}
