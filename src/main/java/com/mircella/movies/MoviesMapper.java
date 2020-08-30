package com.mircella.movies;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MoviesMapper {

    public List<ESMovie> mapToESMovies(List<Movie> movies) {
        return movies.stream().map(this::mapToESMovies).collect(Collectors.toList());
    }

    private ESMovie mapToESMovies(Movie it) {
        return new ESMovie(
                UUID.randomUUID(),
                it.title(),
                it.year(),
                it.released(),
                extractRuntime(it.runtime()),
                Arrays.stream(it.genre().split(", ")).map(Genre::of).collect(Collectors.toSet()),
                Set.of(it.director().split(", ")),
                Set.of(it.actors().split(", ")),
                it.plot(),
                Set.of(it.language().split(", ")),
                mapToCountryCode(it.country()),
                it.ratings()
        );
    }

    private CountryCode mapToCountryCode(String countryName) {
        return Optional.ofNullable(CountryCode.findByName(countryName)
                .stream()
                .findAny()
                .orElseGet(() -> CountryCode.getByAlpha3Code(countryName)))
                .orElse(CountryCode.UNDEFINED);
    }

    public MovieDetails mapToMovieDetails(ESMovie esMovie) {
        return new MovieDetails(
                esMovie.title(),
                esMovie.year(),
                esMovie.released(),
                esMovie.runtime() + " minutes",
                esMovie.genres(),
                esMovie.directors(),
                esMovie.actors(),
                esMovie.plot(),
                esMovie.languages(),
                esMovie.country(),
                esMovie.ratings()
        );
    }

    private int extractRuntime(String runtime) {
        Pattern pattern = Pattern.compile("(^\\d+)");
        Matcher m = pattern.matcher(runtime);
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(0));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public MovieDetails toMovie(SearchHit<ESMovie> searchHit) {
        try {
            return mapToMovieDetails(searchHit.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ESMovie mapToESMovies(MovieDetails movieDetails) {
        return new ESMovie(
                UUID.randomUUID(),
                movieDetails.title(),
                movieDetails.year(),
                movieDetails.released(),
                extractRuntime(movieDetails.runtime()),
                movieDetails.genres(),
                movieDetails.directors(),
                movieDetails.actors(),
                movieDetails.plot(),
                movieDetails.languages(),
                movieDetails.country(),
                movieDetails.ratings()
        );
    }
}
