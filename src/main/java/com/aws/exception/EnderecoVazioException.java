package com.aws.exception;

public class EnderecoVazioException extends CampoObrigatorioException {

	private static final long serialVersionUID = 1L;

	public EnderecoVazioException(String mensagem) {
		super(mensagem);
	}
}
