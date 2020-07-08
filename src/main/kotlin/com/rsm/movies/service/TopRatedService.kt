package com.rsm.movies.service

import JsonMovieBase
import com.google.gson.Gson
import com.rsm.movies.domain.MovieDto
import org.json.simple.JSONObject
import org.springframework.stereotype.Service
import java.util.*

@Service
class TopRatedService : MovieService() {

    companion object {
        const val NUMBER_OF_PAGES = 336
    }

    override fun getMovie(): MovieDto? {
        while (true) {
            val movie = getTopRated(getMovieJson(getEndpointByTopRated(Random().nextInt(NUMBER_OF_PAGES))))
            if (movie != null) {
                return movie
            }
        }
    }

    private fun getEndpointByTopRated(page: Int): String {
        return "https://api.themoviedb.org/3/movie/top_rated?api_key=${API_KEY}&language=en-US&page=${page}"
    }

    private fun getTopRated(json: JSONObject?): MovieDto? {
        val movies = Gson().fromJson(json.toString(), JsonMovieBase::class.java).results
        val movie = movies[Random().nextInt(movies.size)]
        return MovieDto(movie)
    }
}
