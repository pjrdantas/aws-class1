package com.aws.client;

import com.aws.dto.ViaCepResponseDto;

public interface EnderecoClient {

	ViaCepResponseDto buscarPorCep(String cep);
}
