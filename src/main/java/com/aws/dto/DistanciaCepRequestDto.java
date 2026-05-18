package com.aws.dto;

public record DistanciaCepRequestDto(
		CepRequestDto primeira,
		CepRequestDto segunda
) {
}
