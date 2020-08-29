package com.mircella.movies;

import com.neovisionaries.i18n.CountryCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class MoviesMapper {

    public List<ESMovie> mapToESMovie(List<Movie> movies) {
        return movies.stream().map(it -> new ESMovie(
                UUID.randomUUID(),
                it.title(),
                it.year(),
                it.released(),
                extractRuntime(it.runtime()),
                Arrays.stream(it.genre().split("[,| ]")).map(Genre::of).collect(Collectors.toSet()),
                Arrays.asList(it.director().split("[,| ]")),
                Arrays.asList(it.actors().split("[,| ]")),
                it.plot(),
                it.language(),
                CountryCode.getByAlpha2Code(it.country()),
                it.ratings()
        )).collect(Collectors.toList());
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
}
