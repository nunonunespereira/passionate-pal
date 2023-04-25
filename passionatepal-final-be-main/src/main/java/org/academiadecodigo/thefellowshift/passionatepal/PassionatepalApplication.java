package org.academiadecodigo.thefellowshift.passionatepal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
@SpringBootApplication
public class PassionatepalApplication {

	public static void main(String[] args) {
		SpringApplication.run(PassionatepalApplication.class, args);
	}

	@Bean
	public RestTemplate getRestTemplate() {
		var template = new RestTemplate();

		template.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		return template;
	}
}
