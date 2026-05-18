package com.aws.client;

import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpDistanciaClient implements DistanciaClient {

	private final RestClient restClient;

	public HttpDistanciaClient(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("http://localhost:8082")
				.build();
	}

	@Override
	public DistanciaResponseDto calcularDistancia(DistanciaRequestDto request) {
		return restClient.post()
				.uri("/distancia")
				.body(request)
				.retrieve()
				.body(DistanciaResponseDto.class);
	}
}
