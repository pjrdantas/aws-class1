package com.aws.dto;

public record ErroResponseDto(
		int status,
		String erro,
		String mensagem
) {
}
