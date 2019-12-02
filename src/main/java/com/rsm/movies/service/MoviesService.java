package com.rsm.movies.service;

import com.rsm.movies.domain.Movie;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class MoviesService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String API_KEY = "7beb48e03a83a4f72abec70f9c474deb";
    private static final int BOUND_TO_RANDOM = 30000;
    private static final int TIMEOUT = 5000;

    public Movie randomMovie() {
        String jsonMovie = null;

        while (jsonMovie == null) {
            jsonMovie = getJsonMovie();
        }

        return getMovie(jsonMovie);
    }

    private String getJsonMovie() {
        Random random = new Random();
        String endpoint = getEndpoint(random.nextInt(BOUND_TO_RANDOM));
        return getJSON(endpoint);
    }

    private String getEndpoint(int id) {
        return "https://api.themoviedb.org/3/movie/" + id +
                "?api_key=" + API_KEY + "&language=en-US";
    }

    private String getJSON(String endpoint) {
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(endpoint);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-length", "application/json");
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setConnectTimeout(TIMEOUT);
            httpURLConnection.setReadTimeout(TIMEOUT);
            httpURLConnection.connect();
            int status = httpURLConnection.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            httpURLConnection.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    br.close();
                    return sb.toString();
            }
        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception ex) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    private Movie getMovie(String json) {
        Movie movie = new Movie();
        String[] attributes = json.split(",");

        for (String attribute : attributes) {
            if (attribute.contains("original_title")) {
                String[] splited = attribute.split(":");
                String originalTitle = splited[1].substring(1, splited[1].length() - 1);
                movie.setOriginalTitle(originalTitle);
            }

            if (attribute.contains("release_date")) {
                String[] splited = attribute.split(":");
                String releaseDate = splited[1].substring(1, splited[1].length() - 1);
                movie.setReleaseDate(LocalDate.parse(releaseDate, FORMATTER));
            }

            if (attribute.contains("poster_path")) {
                String[] splited = attribute.split(":");
                String posterPath = splited[1].substring(1, splited[1].length() - 1);
                movie.setPosterPath("http://image.tmdb.org/t/p/w185" + posterPath);
            }
        }

        return movie;
    }
}
