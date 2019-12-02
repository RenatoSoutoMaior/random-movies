package com.rsm.movies.controller;

import com.rsm.movies.service.MoviesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MoviesController {

    @Autowired
    private MoviesService moviesService;

    @GetMapping("/random-movie")
    public ResponseEntity<Object> randomMovie() {
        return new ResponseEntity<>(moviesService.randomMovie(), HttpStatus.OK);
    }
}
