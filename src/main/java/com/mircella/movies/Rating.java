package com.mircella.movies;

public record Rating(String source, String value) {
    public Rating() {
        this(null, null);
    }
}
