package com.rsm.movies.controller

import com.rsm.movies.service.MovieService
import com.rsm.movies.service.TopRatedService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class MovieController(var movieService: MovieService) {

    @GetMapping("/top-rated")
    fun getTopRated(): ResponseEntity<Any> {
        movieService = TopRatedService()
        return ResponseEntity(movieService.getMovie(), HttpStatus.OK)
    }

}
