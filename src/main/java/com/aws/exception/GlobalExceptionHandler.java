package com.aws.exception;

import com.aws.dto.ErroResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CampoObrigatorioException.class)
	public ResponseEntity<ErroResponseDto> tratarCampoObrigatorio(CampoObrigatorioException ex) {
		return criarResposta(HttpStatus.BAD_REQUEST, ex.getMessage());
	}

	@ExceptionHandler(RecursoNaoEncontradoException.class)
	public ResponseEntity<ErroResponseDto> tratarRecursoNaoEncontrado(RecursoNaoEncontradoException ex) {
		return criarResposta(HttpStatus.NOT_FOUND, ex.getMessage());
	}

	@ExceptionHandler(ConsultaEnderecoException.class)
	public ResponseEntity<ErroResponseDto> tratarConsultaEndereco(ConsultaEnderecoException ex) {
		return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErroResponseDto> tratarJsonInvalido() {
		return criarResposta(HttpStatus.BAD_REQUEST, "Requisicao invalida.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErroResponseDto> tratarErroInesperado() {
		return criarResposta(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor.");
	}

	private ResponseEntity<ErroResponseDto> criarResposta(HttpStatus httpStatus, String mensagem) {
		ErroResponseDto erro = new ErroResponseDto(
				httpStatus.value(),
				httpStatus.getReasonPhrase(),
				mensagem
		);

		return ResponseEntity.status(httpStatus).body(erro);
	}
}
