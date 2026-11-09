package esprit.gateway;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    /**
     * Routes du point d'entrée unique (port 8080).
     * "lb://NOM" : le gateway demande l'adresse du service à Eureka (load balancer).
     * meeting n'est pas inscrit dans Eureka : on utilise son adresse fixe (propriété meeting.url).
     */
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder,
                                      @Value("${meeting.url}") String meetingUrl) {
        return builder.routes()
                .route("candidat", r -> r.path("/mic1/**")
                        .uri("lb://CANDIDAT"))
                .route("job", r -> r.path("/api/jobs/**", "/api/categories/**")
                        .uri("lb://JOB"))
                .route("canditature", r -> r.path("/mic3/**")
                        .uri("lb://CANDITATURE"))
                .route("notification", r -> r.path("/api/notifications/**")
                        .uri("lb://NOTIFICATION"))
                .route("meeting", r -> r.path("/api/meetings/**")
                        .uri(meetingUrl))
                .build();
    }
}
