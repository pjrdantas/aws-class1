package com.aws.dto;

import org.springframework.http.HttpStatus;

public record EnderecoResultadoDto(
		HttpStatus httpStatus,
		EnderecoResponseDto body
) {

	public static EnderecoResultadoDto ok(EnderecoResponseDto body) {
		return new EnderecoResultadoDto(HttpStatus.OK, body);
	}

	public static EnderecoResultadoDto badRequest(String mensagem) {
		return new EnderecoResultadoDto(HttpStatus.BAD_REQUEST, EnderecoResponseDto.warn(mensagem));
	}

	public static EnderecoResultadoDto notFound(String mensagem) {
		return new EnderecoResultadoDto(HttpStatus.NOT_FOUND, EnderecoResponseDto.warn(mensagem));
	}

	public static EnderecoResultadoDto internalServerError(String mensagem) {
		return new EnderecoResultadoDto(HttpStatus.INTERNAL_SERVER_ERROR, EnderecoResponseDto.warn(mensagem));
	}
}
