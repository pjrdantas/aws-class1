package com.aws.usecase;

import com.aws.client.EnderecoClient;
import com.aws.dto.EnderecoRequestDto;
import com.aws.dto.EnderecoResponseDto;
import com.aws.dto.EnderecoResultadoDto;
import com.aws.dto.ViaCepResponseDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class BuscarEnderecoUseCase {

	private final EnderecoClient enderecoClient;

	public BuscarEnderecoUseCase(EnderecoClient enderecoClient) {
		this.enderecoClient = enderecoClient;
	}

	public EnderecoResultadoDto executar(EnderecoRequestDto request) {
		if (request == null || request.localidade() == null || request.localidade().isBlank()) {
			return EnderecoResultadoDto.badRequest("Localidade nao pode estar vazia.");
		}

		String cep = request.localidade().replaceAll("\\D", "");

		if (cep.isBlank()) {
			return EnderecoResultadoDto.badRequest("Localidade deve conter um CEP valido.");
		}

		try {
			ViaCepResponseDto endereco = enderecoClient.buscarPorCep(cep);

			if (endereco == null || Boolean.TRUE.equals(endereco.erro())) {
				return EnderecoResultadoDto.notFound("Endereco nao encontrado para a localidade informada.");
			}

			return EnderecoResultadoDto.ok(EnderecoResponseDto.success(endereco.localidade()));
		} catch (RestClientException ex) {
			return EnderecoResultadoDto.internalServerError("Nao foi possivel consultar o endereco neste momento.");
		}
	}
}
