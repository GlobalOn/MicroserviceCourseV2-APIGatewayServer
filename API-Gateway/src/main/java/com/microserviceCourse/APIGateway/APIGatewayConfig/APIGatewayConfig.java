package com.microserviceCourse.APIGateway.APIGatewayConfig;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class APIGatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {

//        By adding "lb://currency-exchange-service" we will redirect all path from "patterns:" parameter
//        to loadBalancer (lb) that will go to discovery/naming server and grab service by name defined after
//        "lb://" and add to it path name with all the parameters coming from browser client
        return builder.routes()
                .route(p -> p.path("/get").
                        filters(f -> f.addRequestHeader("MyHeader", "MyURI")).
                        uri("http://httpbin.org:80"))
                .route(p -> p.path("/currency-exchange/**")
                        .uri("lb://currency-exchange-service"))
                .route(p -> p.path("/currency-conversion/**")
                        .uri("lb://currency-conversion-service"))
                .route(p -> p.path("/currency-conversion-feign/**")
                        .uri("lb://currency-conversion-service"))
                .route(p -> p.path("/currency-conversion-new/**")
                        .filters(f->f.rewritePath("/currency-conversion-new/(?<segment>.*)",
                                "/currency-conversion-feign/${segment}"))
                        .uri("lb://currency-conversion-service"))
                .build();
    }
}
