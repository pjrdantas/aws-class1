package com.aws.usecase;

import com.aws.client.CoordenadasClient;
import com.aws.client.DistanciaClient;
import com.aws.dto.CepRequestDto;
import com.aws.dto.CoordenadasResponseDto;
import com.aws.dto.DistanciaCepRequestDto;
import com.aws.dto.DistanciaFormatadaResponseDto;
import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;
import com.aws.exception.CampoObrigatorioException;
import com.aws.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CalcularDistanciaUseCaseTest {

	@Test
	void deveOrquestrarConsultasEFormatarDistancia() {
		List<String> cepsConsultados = new ArrayList<>();
		List<DistanciaRequestDto> requestsDistancia = new ArrayList<>();
		CoordenadasClient coordenadasClient = request -> {
			cepsConsultados.add(request.cep());

			if ("11691024".equals(request.cep())) {
				return new CoordenadasResponseDto(
						"11691-024",
						"Rua Magnolias",
						"Mato Dentro",
						"Ubatuba",
						"SP",
						-23.4406838,
						-45.0911815
				);
			}

			return new CoordenadasResponseDto(
					"01001-000",
					"Praca da Se",
					"Se",
					"Sao Paulo",
					"SP",
					-23.55052,
					-46.633308
			);
		};
		DistanciaClient distanciaClient = request -> {
			requestsDistancia.add(request);
			return new DistanciaResponseDto(5132.0, "Distancia calculada com sucesso");
		};
		CalcularDistanciaUseCase useCase = new CalcularDistanciaUseCase(coordenadasClient, distanciaClient);

		DistanciaFormatadaResponseDto response = useCase.executar(new DistanciaCepRequestDto(
				new CepRequestDto("11691024"),
				new CepRequestDto("01001000")
		));

		assertThat(cepsConsultados).containsExactly("11691024", "01001000");
		assertThat(requestsDistancia).hasSize(1);
		assertThat(requestsDistancia.getFirst().pontoA().latitude()).isEqualTo(-23.4406838);
		assertThat(requestsDistancia.getFirst().pontoA().longitude()).isEqualTo(-45.0911815);
		assertThat(requestsDistancia.getFirst().pontoB().latitude()).isEqualTo(-23.55052);
		assertThat(requestsDistancia.getFirst().pontoB().longitude()).isEqualTo(-46.633308);
		assertThat(response.distancia()).isEqualTo("5km e 132m");
		assertThat(response.mensagem()).isEqualTo("Distancia calculada com sucesso");
	}

	@Test
	void deveFormatarDistanciaMenorQueMilMetros() {
		CoordenadasClient coordenadasClient = request -> new CoordenadasResponseDto(
				request.cep(),
				null,
				null,
				null,
				null,
				-23.0,
				-46.0
		);
		DistanciaClient distanciaClient = request -> new DistanciaResponseDto(999.4, "Distancia calculada com sucesso");
		CalcularDistanciaUseCase useCase = new CalcularDistanciaUseCase(coordenadasClient, distanciaClient);

		DistanciaFormatadaResponseDto response = useCase.executar(new DistanciaCepRequestDto(
				new CepRequestDto("11691024"),
				new CepRequestDto("01001000")
		));

		assertThat(response.distancia()).isEqualTo("999m");
	}

	@Test
	void deveLancarExceptionQuandoCepNaoForInformado() {
		CalcularDistanciaUseCase useCase = new CalcularDistanciaUseCase(request -> null, request -> null);

		assertThatThrownBy(() -> useCase.executar(new DistanciaCepRequestDto(null, new CepRequestDto("01001000"))))
				.isInstanceOf(CampoObrigatorioException.class)
				.hasMessage("O CEP da primeira localidade deve ser informado.");
	}

	@Test
	void deveLancarExceptionQuandoCoordenadasNaoForemEncontradas() {
		CoordenadasClient coordenadasClient = request -> new CoordenadasResponseDto(
				request.cep(),
				null,
				null,
				null,
				null,
				null,
				null
		);
		CalcularDistanciaUseCase useCase = new CalcularDistanciaUseCase(coordenadasClient, request -> null);

		assertThatThrownBy(() -> useCase.executar(new DistanciaCepRequestDto(
				new CepRequestDto("11691024"),
				new CepRequestDto("01001000")
		)))
				.isInstanceOf(RecursoNaoEncontradoException.class)
				.hasMessage("Coordenadas nao encontradas para um dos CEPs informados.");
	}
}
