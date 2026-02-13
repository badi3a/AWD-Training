package esprit.gateway;

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
    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){

       // return builder.routes() .route("candidat",r->r.path("/mic1/**")
                   //     .uri("http://localhost:8081") )
                //.route("candidat",r->r.path("/mic1/**")
                   //     .uri("http://localhost:8090") )
              //  .route("job",r->r.path("/mic2/**")
                   //     .uri("http://localhost:8082") )
               // .route("candidature",r->r.path("/mic3/**")
                 //       .uri("http://localhost:8083") )
              //  .route("meeting",r->r.path("/mic5/**")
                   //     .uri("http://localhost:8085") )
              //  .build();
        //
        return builder.routes() .route("candidat",r->r.path("/mic1/**")
                   .uri("lb://CANDIDAT") )
                .route("job",r->r.path("/mic2/**")
                        .uri("lb://JOB") )
                .route("candidature",r->r.path("/mic3/**")
                        .uri("lb://CANDIDATURE") )
                .route("notification",r->r.path("/mic4/" +
                                "**")
                        .uri("lb://NOTIFICATION") )
                .route("meeting",r->r.path("/mic5/**")
                        .uri("lb://MEETING") )
                .build();
         }
}
