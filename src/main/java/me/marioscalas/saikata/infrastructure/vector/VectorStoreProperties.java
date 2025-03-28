package me.marioscalas.saikata.infrastructure.vector;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "sfg.aiapp")
@Getter @Setter
public class VectorStoreProperties {
    private String vectorStorePath;
    private List<Resource> documentsToLoad;
}
