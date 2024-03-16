package com.site.ota;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

        @Override
        public void addResourceHandlers(final ResourceHandlerRegistry registry) {
            registry.addResourceHandler("/files/**")
                    .addResourceLocations("file:src/main/resources/templates/", "file:src/main/resources/static/", "file:///home/administrator/Test/ota (2)/files/");
        }
   }
