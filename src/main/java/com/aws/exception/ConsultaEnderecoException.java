package com.aws.exception;

public class ConsultaEnderecoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ConsultaEnderecoException(String mensagem, Throwable cause) {
		super(mensagem, cause);
	}
}
