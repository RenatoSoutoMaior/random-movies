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

@Service
class MoviesService() {

    private val FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val API_KEY = "7beb48e03a83a4f72abec70f9c474deb"
    private val BOUND_TO_RANDOM = 30000
    private val TIMEOUT = 5000

    fun randomMovie(): MovieDto? {
        var jsonMovie: String? = null
        while (jsonMovie == null) {
            jsonMovie = getJsonMovie()
        }
        return getMovie(jsonMovie)
    }

    private fun getJsonMovie(): String? {
        val random = Random()
        val endpoint = getEndpoint(random.nextInt(BOUND_TO_RANDOM))
        return getJSON(endpoint)
    }

    private fun getEndpoint(id: Int): String {
        return "https://api.themoviedb.org/3/movie/" + id +
                "?api_key=" + API_KEY + "&language=en-US"
    }

    private fun getJSON(endpoint: String): String? {
        var httpURLConnection: HttpURLConnection? = null
        try {
            val url = URL(endpoint)
            httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection!!.setRequestProperty("Content-length", "application/json")
            httpURLConnection.useCaches = false
            httpURLConnection.allowUserInteraction = false
            httpURLConnection.connectTimeout = TIMEOUT
            httpURLConnection.readTimeout = TIMEOUT
            httpURLConnection.connect()
            val status = httpURLConnection.responseCode
            when (status) {
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

    private fun getMovie(json: String): MovieDto? {
        val movie = MovieDto()
        val attributes = json.split(",").toTypedArray()
        for (attribute in attributes) {
            if (attribute.contains("original_title")) {
                val splited = attribute.split(":").toTypedArray()
                val originalTitle = splited[1].substring(1, splited[1].length - 1)
                movie.originalTitle = originalTitle
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

}