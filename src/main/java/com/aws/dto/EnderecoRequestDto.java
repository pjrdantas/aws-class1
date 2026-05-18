package com.aws.dto;

public record EnderecoRequestDto(String localidade, CepDto primeira, CepDto segunda) {
	public EnderecoRequestDto(String localidade) {
		this(localidade, null, null);
	}

	public String cepParaConsulta() {
		if (localidade != null && !localidade.isBlank()) {
			return localidade;
		}

		if (primeira != null && primeira.cep() != null && !primeira.cep().isBlank()) {
			return primeira.cep();
		}

		if (segunda != null && segunda.cep() != null && !segunda.cep().isBlank()) {
			return segunda.cep();
		}

		return null;
	}

	public record CepDto(String cep) {
	}
}
