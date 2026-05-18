package com.aws.usecase;

import com.aws.client.CoordenadasClient;
import com.aws.client.DistanciaClient;
import com.aws.dto.CepRequestDto;
import com.aws.dto.CoordenadasResponseDto;
import com.aws.dto.DistanciaCepRequestDto;
import com.aws.dto.DistanciaFormatadaResponseDto;
import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import com.aws.dto.PontoDto;
import com.aws.exception.CampoObrigatorioException;
import com.aws.exception.ConsultaEnderecoException;
import com.aws.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

@Service
public class CalcularDistanciaUseCase {

	private final CoordenadasClient coordenadasClient;
	private final DistanciaClient distanciaClient;

	public CalcularDistanciaUseCase(CoordenadasClient coordenadasClient, DistanciaClient distanciaClient) {
		this.coordenadasClient = coordenadasClient;
		this.distanciaClient = distanciaClient;
	}

	public DistanciaFormatadaResponseDto executar(DistanciaCepRequestDto request) {
		validarRequest(request);

		try {
			CoordenadasResponseDto primeiraCoordenada = buscarCoordenadas(request.primeira());
			CoordenadasResponseDto segundaCoordenada = buscarCoordenadas(request.segunda());

			DistanciaResponseDto distancia = distanciaClient.calcularDistancia(new DistanciaRequestDto(
					criarPonto(primeiraCoordenada),
					criarPonto(segundaCoordenada)
			));

			if (distancia == null || distancia.distanciaMetros() == null) {
				throw new ConsultaEnderecoException("Servico de distancia retornou uma resposta invalida.", null);
			}

			return new DistanciaFormatadaResponseDto(
					formatarDistancia(distancia.distanciaMetros()),
					distancia.mensagem()
			);
		} catch (RestClientResponseException ex) {
			if (ex.getStatusCode().isSameCodeAs(HttpStatus.NOT_FOUND)) {
				throw new RecursoNaoEncontradoException("Coordenadas nao encontradas para um dos CEPs informados.");
			}

			throw new ConsultaEnderecoException("Nao foi possivel orquestrar o calculo de distancia neste momento.", ex);
		} catch (RestClientException ex) {
			throw new ConsultaEnderecoException("Nao foi possivel orquestrar o calculo de distancia neste momento.", ex);
		}
	}

	private void validarRequest(DistanciaCepRequestDto request) {
		if (request == null) {
			throw new CampoObrigatorioException("Os CEPs devem ser informados.");
		}

		validarCep(request.primeira(), "primeira");
		validarCep(request.segunda(), "segunda");
	}

	private void validarCep(CepRequestDto request, String campo) {
		if (request == null || request.cep() == null || request.cep().isBlank()) {
			throw new CampoObrigatorioException("O CEP da " + campo + " localidade deve ser informado.");
		}
	}

	private CoordenadasResponseDto buscarCoordenadas(CepRequestDto request) {
		CoordenadasResponseDto coordenadas = coordenadasClient.buscarCoordenadas(request);

		if (coordenadas == null || coordenadas.latitude() == null || coordenadas.longitude() == null) {
			throw new RecursoNaoEncontradoException("Coordenadas nao encontradas para um dos CEPs informados.");
		}

		return coordenadas;
	}

	private PontoDto criarPonto(CoordenadasResponseDto coordenadas) {
		return new PontoDto(coordenadas.latitude(), coordenadas.longitude());
	}

	private String formatarDistancia(Double distanciaMetros) {
		long metros = Math.round(distanciaMetros);

		if (metros < 1000) {
			return metros + "m";
		}

		long kilometros = metros / 1000;
		long metrosRestantes = metros % 1000;

		if (metrosRestantes == 0) {
			return kilometros + "km";
		}

		return kilometros + "km e " + metrosRestantes + "m";
	}
}
