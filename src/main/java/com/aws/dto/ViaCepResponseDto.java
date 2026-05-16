package com.aws.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ViaCepResponseDto(
		String logradouro,
		String localidade,
		Boolean erro
) {
}
