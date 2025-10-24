package com.example.managebyclesystem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRootPath = System.getProperty("user.dir");
        Path uploadDir = Paths.get(projectRootPath, "uploads");
        String uploadPath = uploadDir.toFile().getAbsolutePath();

        System.out.println(">>> Serving uploaded files from: " + uploadPath);

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/"); // Use "file:" prefix

    }
}