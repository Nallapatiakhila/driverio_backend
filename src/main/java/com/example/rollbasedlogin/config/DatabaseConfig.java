package com.example.rollbasedlogin.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.jdbc.DataSourceBuilder;
import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        String dbUrl = System.getenv("DB_URL");
        if (dbUrl == null || dbUrl.isEmpty()) {
            dbUrl = System.getenv("DATABASE_URL");
        }
        
        String username = System.getenv("DB_USERNAME");
        if (username == null || username.isEmpty()) {
            username = System.getenv("DATABASE_USERNAME");
        }
        
        String password = System.getenv("DB_PASSWORD");
        if (password == null || password.isEmpty()) {
            password = System.getenv("DATABASE_PASSWORD");
        }

        String jdbcUrl = null;

        if (dbUrl != null && !dbUrl.isEmpty()) {
            if (dbUrl.startsWith("postgres://") || dbUrl.startsWith("postgresql://")) {
                try {
                    String cleanedUrl = dbUrl;
                    if (dbUrl.startsWith("postgres://")) {
                        cleanedUrl = "postgresql://" + dbUrl.substring("postgres://".length());
                    }
                    URI uri = new URI(cleanedUrl);
                    String host = uri.getHost();
                    int port = uri.getPort();
                    if (port == -1) {
                        port = 5432;
                    }
                    String path = uri.getPath();
                    
                    jdbcUrl = "jdbc:postgresql://" + host + ":" + port + path;
                    
                    String userInfo = uri.getUserInfo();
                    if (userInfo != null && userInfo.contains(":")) {
                        String[] parts = userInfo.split(":");
                        // Always prioritize credentials embedded inside the URL itself
                        username = parts[0];
                        password = parts[1];
                    }
                } catch (URISyntaxException e) {
                    String rawUrl = dbUrl;
                    if (rawUrl.startsWith("postgres://")) {
                        rawUrl = "postgresql://" + rawUrl.substring("postgres://".length());
                    }
                    jdbcUrl = "jdbc:" + rawUrl;
                }
            } else if (dbUrl.startsWith("jdbc:")) {
                jdbcUrl = dbUrl;
            } else {
                jdbcUrl = "jdbc:" + dbUrl;
            }
        }

        if (jdbcUrl == null) {
            jdbcUrl = "jdbc:mysql://localhost:3306/rolebased_login?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        }
        if (username == null) {
            username = "root";
        }
        if (password == null) {
            password = "root";
        }

        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
