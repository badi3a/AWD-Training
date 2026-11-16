package esprit.canditature;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
public class CanditatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(CanditatureApplication.class, args);
	}

}
