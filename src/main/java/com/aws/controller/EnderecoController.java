package com.aws.controller;

import com.aws.dto.EnderecoRequestDto;
import com.aws.dto.EnderecoResponseDto;
import com.aws.dto.EnderecoResultadoDto;
import com.aws.usecase.BuscarEnderecoUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

	private final BuscarEnderecoUseCase buscarEnderecoUseCase;

	public EnderecoController(BuscarEnderecoUseCase buscarEnderecoUseCase) {
		this.buscarEnderecoUseCase = buscarEnderecoUseCase;
	}

	@PostMapping
	public ResponseEntity<EnderecoResponseDto> buscarEndereco(@RequestBody(required = false) EnderecoRequestDto request) {
		EnderecoResultadoDto resultado = buscarEnderecoUseCase.executar(request);

		return ResponseEntity.status(resultado.httpStatus()).body(resultado.body());
	}
}
