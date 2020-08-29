package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @PostMapping("/movies")
    public ResponseEntity insertMovies(){
        moviesService.insertMovies();
        return ResponseEntity.ok("Movies inserted");
    }
}
