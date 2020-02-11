package com.rsm.movies.controller

import com.rsm.movies.service.MoviesService
import com.rsm.movies.service.RandomService
import com.rsm.movies.service.TopRatedService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/movies")
class MoviesController(var moviesService: MoviesService) {

    @GetMapping("/random-movie")
    fun getRandomMovie(): ResponseEntity<Any> {
        moviesService = RandomService()
        return ResponseEntity(moviesService.getMovie(), HttpStatus.OK)
    }

    @GetMapping("/top-rated")
    fun getTopRatedMovie(): ResponseEntity<Any> {
        moviesService = TopRatedService()
        return ResponseEntity(moviesService.getMovie(), HttpStatus.OK)
    }

}
