package com.rsm.movies.service

import com.rsm.movies.domain.MovieDto
import org.json.simple.JSONObject
import org.springframework.stereotype.Service
import java.util.Random

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
                val releaseDate = splited[1].substring(1).split("\"")[0]
                movie.release_date = releaseDate
                countDate += 1
            }

            if (countPoster == 0 && attribute.contains("poster_path")) {
                val posterPath = splited[1].split(".")[0].split("/")[splited.size - 1].plus(".jpg")
                movie.poster_path = ("/$posterPath")
                countPoster += 1
            }

            if (countTitle == 1 && countDate == 1 && countPoster == 1) {
                movies.add(movie)
                movie = MovieDto()
                countTitle = 0
                countDate = 0
                countPoster = 0
            }
        }

        return movies[Random().nextInt(movies.size)]
    }
}
