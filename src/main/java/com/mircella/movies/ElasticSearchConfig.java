package com.mircella.movies;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import java.text.SimpleDateFormat;

@Configuration
@RequiredArgsConstructor
class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

    private final ElasticSearchSettings settings;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(settings.getHost(), settings.getPort(), settings.getScheme()))
                        .setRequestConfigCallback(new RestClientBuilder.RequestConfigCallback() {
                            @Override
                            public RequestConfig.Builder customizeRequestConfig(RequestConfig.Builder requestConfigBuilder) {
                                return requestConfigBuilder.setSocketTimeout(settings.getTimeout());
                            }
                        })
        );
    }

    @Bean
    public ObjectMapper objectMapper() {
        var mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setVisibility(VisibilityChecker.Std.defaultInstance().withFieldVisibility(JsonAutoDetect.Visibility.ANY));
        return mapper;
    }
}
