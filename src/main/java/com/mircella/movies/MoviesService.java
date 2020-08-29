package com.mircella.movies;

import lombok.RequiredArgsConstructor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class MoviesService {

    private final MoviesRepository moviesRepository;
    private final MoviesMapper moviesMapper;
    private final ElasticsearchOperations elasticsearchOperations;
    private final IndexOperations moviesIndexOperations;

    public MoviesService(MoviesRepository moviesRepository, MoviesMapper moviesMapper, ElasticsearchOperations elasticsearchOperations) {
        this.moviesRepository = moviesRepository;
        this.moviesMapper = moviesMapper;
        this.elasticsearchOperations = elasticsearchOperations;
        this.moviesIndexOperations = elasticsearchOperations.indexOps(IndexCoordinates.of("movies"));
    }

    public void insertMovies() {
        var movies = moviesMapper.mapToESMovie(moviesRepository.loadMovies());
        var indexQueries = movies.stream().map(movie ->
                new IndexQueryBuilder()
                        .withId(movie.id().toString())
                        .withObject(movie)
                        .build()
        ).collect(Collectors.toList());
        elasticsearchOperations.bulkIndex(indexQueries, IndexCoordinates.of("movies"));
        moviesIndexOperations.refresh();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
