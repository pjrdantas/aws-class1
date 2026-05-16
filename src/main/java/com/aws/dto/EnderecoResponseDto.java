package com.aws.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record EnderecoResponseDto(
		String mensagem,
		String cidade
) {

	public static EnderecoResponseDto warn(String mensagem) {
		return new EnderecoResponseDto(mensagem, null);
	}

	public static EnderecoResponseDto success(String cidade) {
		return new EnderecoResponseDto(null, cidade);
	}
}
