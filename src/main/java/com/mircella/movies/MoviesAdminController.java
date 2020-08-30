package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MoviesAdminController {

    private final MoviesIndexOperations indexClient;

    @PostMapping("/admin/movies")
    public ResponseEntity<String> createMoviesIndex() {
        indexClient.createMovieIndex();
        return ResponseEntity.ok("Movies Index Created");
    }

    @DeleteMapping("/admin/movies")
    public ResponseEntity<String> deleteMoviesIndex() {
        indexClient.deleteMovieIndex();
        return ResponseEntity.ok("Movies Index Deleted");
    }

    @GetMapping("/admin/movies")
    public ResponseEntity<String> checkMoviesIndex() {
        boolean exists = indexClient.moviesIndexExists();
        if (exists) {
            return ResponseEntity.ok("Movies Index Exists");
        } else {
            return ResponseEntity.ok("Movies Index Does Not Exist");
        }
    }


}
