package com.aws.controller;

import com.aws.dto.DistanciaCepRequestDto;
import com.aws.dto.DistanciaFormatadaResponseDto;
import com.aws.usecase.CalcularDistanciaUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/distancias", "/ceps"})
public class DistanciaController {

	private final CalcularDistanciaUseCase calcularDistanciaUseCase;

	public DistanciaController(CalcularDistanciaUseCase calcularDistanciaUseCase) {
		this.calcularDistanciaUseCase = calcularDistanciaUseCase;
	}

	@PostMapping({"/ceps", "/coordenadas"})
	public ResponseEntity<DistanciaFormatadaResponseDto> calcular(@RequestBody(required = false) DistanciaCepRequestDto request) {
		return ResponseEntity.ok(calcularDistanciaUseCase.executar(request));
	}
}
