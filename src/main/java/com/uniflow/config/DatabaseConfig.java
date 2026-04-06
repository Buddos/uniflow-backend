package com.uniflow.config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.hikari")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        
        // Log the original URL (masking credentials if any, but Railway internal URLs are usually clear here)
        logger.info("Initializing DataSource with URL: {}", url);

        if (url != null && url.startsWith("postgresql://")) {
            String fixedUrl = "jdbc:" + url;
            logger.info("Fixed Railway PostgreSQL URL: {}", fixedUrl);
            properties.setUrl(fixedUrl);
        } else if (url == null || url.isEmpty()) {
            logger.error("DataSource URL is empty! Please check your environment variables (DATABASE_URL, PGHOST, etc.)");
        }

        return properties.initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }
}
