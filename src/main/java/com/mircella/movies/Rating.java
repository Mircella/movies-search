package com.mircella.movies;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record Rating(String source, String value) {
    public Rating() {
        this(null, null);
    }
}
