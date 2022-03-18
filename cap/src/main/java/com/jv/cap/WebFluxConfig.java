package com.jv.cap;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
//@EnableWebFlux
public class WebFluxConfig implements WebFluxConfigurer {


	
	@Bean(name = "getWebClient1")
	public WebClient getWebClient1() {
		return WebClient.builder().baseUrl("http://localhost:8081")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}

	@Bean(name = "getWebClient2")
	public WebClient getWebClient2() {
		return WebClient.builder().baseUrl("http://localhost:8082")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Bean(name = "getWebClient3")
	public WebClient getWebClient3() {
		return WebClient.builder().baseUrl("http://localhost:8083")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Bean(name = "getWebClient4")
	public WebClient getWebClient4() {
		return WebClient.builder().baseUrl("http://localhost:8084")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}
	
	@Bean(name = "getWebClient5")
	public WebClient getWebClient5() {
		return WebClient.builder().baseUrl("http://localhost:8085")
				.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE).build();
	}	
	


}
