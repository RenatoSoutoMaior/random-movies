package com.rsm.movies.service

import com.google.gson.Gson
import com.rsm.movies.domain.MovieDto
import org.json.simple.JSONObject
import java.util.Random

class RandomService : MoviesService() {

    override fun getMovie(): MovieDto? {
        while (true) {
            val movie = getMovie(getMovieJson(getEndpointById(Random().nextInt(30000))))
            if (movie != null) {
                return movie
            }
        }
    }

    private fun getMovie(json: JSONObject?): MovieDto? {
        return Gson().fromJson(json.toString(), MovieDto::class.java)
    }

    private fun getEndpointById(id: Int): String {
        return "https://api.themoviedb.org/3/movie/${id}?api_key=${API_KEY}&language=en-US"
    }
}
