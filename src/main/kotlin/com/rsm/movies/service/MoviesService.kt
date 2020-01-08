package com.rsm.movies.service

import com.rsm.movies.domain.MovieDto
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Level
import java.util.logging.Logger
import kotlin.collections.ArrayList

@Service
class MoviesService() {

    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val API_KEY = "7beb48e03a83a4f72abec70f9c474deb"
    private val TIMEOUT = 5000

    fun getRandomMovie(): MovieDto? {
        var jsonMovie: String? = null
        while (jsonMovie == null) {
            jsonMovie = getJSON(getEndpointByMovieId(Random().nextInt(30000)))
        }
        return getMovieById(jsonMovie)
    }

    fun getTopRatedMovie(): MovieDto? {
        var jsonMovie: String? = null
        while (jsonMovie == null) {
            val randomPage = Random().nextInt(336)

            jsonMovie = if (randomPage <= 0) {
                getJSON(getEndpointByTopRated(1))
            } else {
                getJSON(getEndpointByTopRated(randomPage))
            }
        }
        return getMovieByTopRated(jsonMovie)
    }

    private fun getEndpointByMovieId(id: Int): String {
        return "https://api.themoviedb.org/3/movie/${id}?api_key=${API_KEY}&language=en-US"
    }

    private fun getEndpointByTopRated(page: Int): String {
        return "https://api.themoviedb.org/3/movie/top_rated?api_key=${API_KEY}&language=en-US&page=${page}"
    }

    private fun getJSON(endpoint: String): String? {
        var httpURLConnection: HttpURLConnection? = null
        try {
            val url = URL(endpoint)
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.setRequestProperty("Content-length", "application/json")
            httpURLConnection.useCaches = false
            httpURLConnection.allowUserInteraction = false
            httpURLConnection.connectTimeout = TIMEOUT
            httpURLConnection.readTimeout = TIMEOUT
            httpURLConnection.connect()
            when (httpURLConnection.responseCode) {
                200, 201 -> {
                    val br = BufferedReader(InputStreamReader(
                            httpURLConnection.inputStream))
                    val sb = StringBuilder()
                    var line: String?
                    while (br.readLine().also { line = it } != null) {
                        sb.append(line).append("\n")
                    }
                    br.close()
                    return sb.toString()
                }
            }
        } catch (ex: IOException) {
            Logger.getLogger(javaClass.name).log(Level.SEVERE, null, ex)
        } finally {
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect()
                } catch (ex: Exception) {
                    Logger.getLogger(javaClass.name).log(Level.SEVERE, null, ex)
                }
            }
        }
        return null
    }

    private fun getMovieById(json: String): MovieDto? {
        val movie = MovieDto()
        val attributes = json.split(",").toTypedArray()
        for (attribute in attributes) {
            if (!attribute.contains("original_title") && attribute.contains("title")) {
                val splited = attribute.split(":").toTypedArray()
                val title = splited[1].substring(1, splited[1].length - 1)
                movie.title = title
            }
            if (attribute.contains("release_date")) {
                val splited = attribute.split(":").toTypedArray()
                val releaseDate = splited[1].substring(1, splited[1].length - 1)
                movie.releaseDate = (LocalDate.parse(releaseDate, FORMATTER))
            }
            if (attribute.contains("poster_path")) {
                val splited = attribute.split(":").toTypedArray()
                val posterPath = splited[1].substring(1, splited[1].length - 1)
                movie.posterPath = ("http://image.tmdb.org/t/p/w185$posterPath")
            }
        }
        return movie
    }

    private fun getMovieByTopRated(json: String): MovieDto? {
        val movies = ArrayList<MovieDto>()
        var movie = MovieDto()
        var countTitle = 0
        var countDate = 0
        var countPoster = 0
        val attributes = json.split("[{").toTypedArray()[1].split(",").toTypedArray()
        for (attribute in attributes) {
            if (!attribute.contains("original_title") && attribute.contains("title") && countTitle == 0) {
                val splited = attribute.split(":").toTypedArray()
                val title = splited[1].substring(1, splited[1].length - 1)
                movie.title = title
                countTitle += 1
            }
            if (attribute.contains("release_date") && countDate == 0) {
                val splited = attribute.split(":").toTypedArray()
                val releaseDate = splited[1].substring(1, 11)
                movie.releaseDate = (LocalDate.parse(releaseDate, FORMATTER))
                countDate += 1
            }
            if (attribute.contains("poster_path") && countPoster == 0) {
                val splited = attribute.split(":").toTypedArray()
                val posterPath = splited[1].split(".")[0].substring(2).plus(".jpg")
                movie.posterPath = ("http://image.tmdb.org/t/p/w185$posterPath")
                countPoster += 1
            }
            if (countTitle == 1 && countDate == 1 && countPoster == 1) {
                movies.add(MovieDto(movie.title, movie.releaseDate, movie.posterPath))
                movie = MovieDto()
                countTitle = 0
                countDate = 0
                countPoster = 0
            }
        }

        return movies[Random().nextInt(movies.size - 1)]
    }

}
