package com.rsm.movies.service

import com.rsm.movies.domain.MovieDto
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Level
import java.util.logging.Logger


@Service
abstract class MoviesService {

    val API_KEY = "7beb48e03a83a4f72abec70f9c474deb"
    val TIMEOUT = 5000

    abstract fun getMovie(): MovieDto?

    fun getMovieJson(endpoint: String): JSONObject? {
        return try {
            JSONParser().parse(getJSON(endpoint)) as JSONObject?
        } catch (e: NullPointerException) {
            null
        }
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
                    val br = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
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
}
