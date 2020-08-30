package com.mircella.movies;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

public record Movie(String title,
                    int year,
                    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd MMM yyyy")
                    LocalDate released,
                    String runtime,
                    String genre,
                    String director,
                    String actors,
                    String plot,
                    String language,
                    String country,
                    Set<Rating> ratings) {

    public Movie() {
        this(
                null,
                0,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                Collections.emptySet()
        );
    }
}
