package com.aws.dto;

public record CoordenadasResponseDto(
		String cep,
		String rua,
		String bairro,
		String cidade,
		String estado,
		Double latitude,
		Double longitude
) {
}
