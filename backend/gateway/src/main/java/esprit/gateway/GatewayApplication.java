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
	@Bean //
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
		//EUREKA + Gateway
		//roundRobin: LoadBalancer
		return builder.routes()
				.route("Candidat",r->r.path("/api/candidates/**")
						.uri("lb://CANDIDAT") )
				.route("Job", r->r.path("/api/jobs/**")
						.uri("lb://JOB") )
				.build();
	}
}
