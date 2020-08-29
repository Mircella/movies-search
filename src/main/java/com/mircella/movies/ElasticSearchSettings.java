package com.mircella.movies;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticSearchSettings {

    @NonNull
    private String host;
    private int port;
    @NonNull
    private String scheme;
    @NonNull
    private String clusterName;
    private int timeout;
}
