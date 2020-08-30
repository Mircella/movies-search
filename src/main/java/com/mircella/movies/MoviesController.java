package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
public class MoviesController {

    private final MoviesService moviesService;

    @PostMapping("/movies/bulk")
    public ResponseEntity<String> insertMovies() {
        moviesService.insertMovies();
        return ResponseEntity.ok("Movies inserted");
    }

    @GetMapping("/movies")
    public ResponseEntity<Flux<MovieDetails>> getMovies() {
        Flux<MovieDetails> movies = moviesService.getMovies();
        return ResponseEntity.ok().body(movies);
    }

    @PostMapping("/movies")
    public ResponseEntity<Mono<UUID>> insertMovie(@RequestBody MovieDetails movieDetails) {
        var saved = moviesService.insertMovie(movieDetails);
        return ResponseEntity.ok().body(saved);
    }

    @GetMapping("/movies/{title}")
    public ResponseEntity<Mono<MovieDetails>> getMovieByTitle(@PathVariable String title) {
        var movie = moviesService.getMovieByTitle(title);
        return ResponseEntity.ok().body(movie);
    }

    @DeleteMapping("/movies/{title}")
    public ResponseEntity<Mono<UUID>> deleteMovie(@PathVariable String title) {
        var deleted = moviesService.deleteMovie(title);
        return ResponseEntity.ok().body(deleted);
    }

    @PutMapping("/movies/{title}")
    public ResponseEntity<Mono<UUID>> updateMovie(@PathVariable String title) {
        var updated = moviesService.updateMovie(title);
        return ResponseEntity.ok().body(updated);
    }
}
