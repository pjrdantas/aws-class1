package com.aws.client;

import com.aws.dto.ViaCepResponseDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ViaCepEnderecoClient implements EnderecoClient {

	private final RestClient restClient;

	public ViaCepEnderecoClient(RestClient.Builder restClientBuilder) {
		this.restClient = restClientBuilder
				.baseUrl("https://viacep.com.br/ws")
				.build();
	}

	@Override
	public ViaCepResponseDto buscarPorCep(String cep) {
		return restClient.get()
				.uri("/{cep}/json", cep)
				.retrieve()
				.body(ViaCepResponseDto.class);
	}
}
