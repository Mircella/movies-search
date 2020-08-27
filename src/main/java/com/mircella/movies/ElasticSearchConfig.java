package com.mircella.movies;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ElasticSearchConfig {

    private final ElasticSearchSettings settings;

    ElasticSearchConfig(ElasticSearchSettings settings) {
        this.settings = settings;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient() {
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
}
