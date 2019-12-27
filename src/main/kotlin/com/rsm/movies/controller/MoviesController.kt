package com.rsm.movies.controller

import com.rsm.movies.service.MoviesService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController()
@RequestMapping("/movies")
class MoviesController(val moviesService: MoviesService) {

    @GetMapping("/random-movie")
    fun randomMovie(): ResponseEntity<Any> {
        return ResponseEntity(moviesService.randomMovie(), HttpStatus.OK)
    }

}