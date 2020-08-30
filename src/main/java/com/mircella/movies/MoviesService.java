package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.core.ReactiveElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MoviesService {

    private final MoviesRepository moviesRepository;
    private final MoviesMapper moviesMapper;
    private final MoviesIndexOperations moviesIndexOperations;
    private final ReactiveElasticsearchOperations reactiveElasticsearchOperations;

    public void insertMovies() {
        if (moviesIndexOperations.moviesIndexExists()) {
            var movies = moviesMapper.mapToESMovies(moviesRepository.loadMovies());
            reactiveElasticsearchOperations.saveAll(movies, IndexCoordinates.of("movies")).subscribe();
            moviesIndexOperations.refresh();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Flux<MovieDetails> getMovies() {
        if (moviesIndexOperations.moviesIndexExists()) {
            var getAllQuery = createQuery(QueryBuilders.matchAllQuery());
            return getMovies(getAllQuery);
        }
        return Flux.empty();
    }

    public Mono<UUID> insertMovie(MovieDetails movieDetails) {
        var movie = moviesMapper.mapToESMovies(movieDetails);
        return reactiveElasticsearchOperations.save(movie).map(ESMovie::id);
    }

    public Mono<MovieDetails> getMovieByTitle(String title) {
        return Mono.just(moviesMapper.mapToMovieDetails(getESMovieByTitle(title)));
    }

    public Mono<UUID> deleteMovie(String title) {
        var movieId = getESMovieByTitle(title).id();
        return reactiveElasticsearchOperations.delete(movieId.toString(), IndexCoordinates.of("movies")).map(UUID::fromString);
    }

    private ESMovie getESMovieByTitle(String title) {
        var queryBuilder = createMatchQuery(title);
        var query = createQuery(queryBuilder);
        var results = Optional.ofNullable(
                reactiveElasticsearchOperations.search(query, ESMovie.class)
                        .collectList()
                        .block())
                .orElse(Collections.emptyList());
        return getMovieWithHighestScore(results);
    }

    public Mono<UUID> updateMovie(String title) {
        return null;
    }

    private NativeSearchQuery createQuery(QueryBuilder query) {
        return new NativeSearchQueryBuilder().withQuery(query).build();
    }

    private QueryBuilder createMatchFuzzyQuery(String query) {
        return QueryBuilders.matchQuery("title", query).fuzziness(Fuzziness.AUTO);
    }

    private QueryBuilder createMatchQuery(String query) {
        return new MatchQueryBuilder("title", query);
    }

    private Flux<MovieDetails> getMovies(NativeSearchQuery query) {
        return reactiveElasticsearchOperations.search(query, ESMovie.class).map(moviesMapper::toMovie);
    }

    private ESMovie getMovieWithHighestScore(List<SearchHit<ESMovie>> movies) {
        Map<Float, ESMovie> moviesWithScores = movies.stream()
                .collect(Collectors.toMap(
                        SearchHit::getScore,
                        SearchHit::getContent,
                        (a, b) -> {
                            log.error("Found movies with same score:{},{}", a, b);
                            return b;
                        }));
        var maxScore = moviesWithScores.keySet().stream().max(new Comparator<Float>() {
            @Override
            public int compare(Float o1, Float o2) {
                return o1.compareTo(o2);
            }
        });
        var movie = maxScore.map(moviesWithScores::get);
        return movie.orElse(null);

    }
}
