package com.aws.client;

import com.aws.dto.CepRequestDto;
import com.aws.dto.CoordenadasResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class HttpCoordenadasClient implements CoordenadasClient {

	private final RestClient restClient;

	public HttpCoordenadasClient(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("http://localhost:8081")
				.build();
	}

	@Override
	public CoordenadasResponseDto buscarCoordenadas(CepRequestDto request) {
		return restClient.post()
				.uri("/ceps/coordenadas")
				.body(request)
				.retrieve()
				.body(CoordenadasResponseDto.class);
	}
}
