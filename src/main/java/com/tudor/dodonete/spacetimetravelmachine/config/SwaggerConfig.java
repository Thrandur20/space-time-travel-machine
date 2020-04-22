package com.tudor.dodonete.spacetimetravelmachine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.client.LinkDiscoverer;
import org.springframework.hateoas.client.LinkDiscoverers;
import org.springframework.hateoas.mediatype.collectionjson.CollectionJsonLinkDiscoverer;
import org.springframework.plugin.core.SimplePluginRegistry;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.*;
import java.util.function.Predicate;

@Configuration
@EnableSwagger2WebMvc
public class SwaggerConfig {
    public static final Contact DEFAULT_CONTACT = new Contact(
            "Tudor Dodonete",
            "https://github.com/Thrandur20/space-time-travel-machine",
            "tudor.dodonete@gmail.com"
    );

    public static final ApiInfo DEFAULT_API_INFO = new ApiInfo(
            "Space Time Travel Machine API",
            "The API for the Space Time Travel Machine exercise",
            "1.0",
            "urn:tos",
            DEFAULT_CONTACT,
            "Apache 2.0",
            "http://www.apache.org/licenses/LICENSE-2.0",
            new ArrayList<>()
    );

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES =
            new HashSet<>(Arrays.asList("application/json", "application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES)
                .select()
                .paths(Predicate.not(PathSelectors.regex("/error.*")))
                .build();
    }


}
