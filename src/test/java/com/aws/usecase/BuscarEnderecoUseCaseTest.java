package com.aws.usecase;

import com.aws.client.EnderecoClient;
import com.aws.dto.EnderecoRequestDto;
import com.aws.dto.EnderecoResultadoDto;
import com.aws.dto.ViaCepResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;

class BuscarEnderecoUseCaseTest {

	@Test
	void deveRetornarWarnQuandoLocalidadeEstiverVazia() {
		BuscarEnderecoUseCase useCase = new BuscarEnderecoUseCase(cep -> null);

		EnderecoResultadoDto response = useCase.executar(new EnderecoRequestDto(""));

		assertThat(response.httpStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(response.body().mensagem()).isEqualTo("Localidade nao pode estar vazia.");
		assertThat(response.body().cidade()).isNull();
	}

	@Test
	void deveRetornarSomenteCidadeQuandoCepExistir() {
		EnderecoClient enderecoClient = cep -> new ViaCepResponseDto("Rua Magnolias", "Ubatuba", false);
		BuscarEnderecoUseCase useCase = new BuscarEnderecoUseCase(enderecoClient);

		EnderecoResultadoDto response = useCase.executar(new EnderecoRequestDto("11691-024"));

		assertThat(response.httpStatus()).isEqualTo(HttpStatus.OK);
		assertThat(response.body().mensagem()).isNull();
		assertThat(response.body().cidade()).isEqualTo("Ubatuba");
	}

	@Test
	void deveRetornarWarnQuandoCepNaoForEncontrado() {
		EnderecoClient enderecoClient = cep -> new ViaCepResponseDto(null, null, true);
		BuscarEnderecoUseCase useCase = new BuscarEnderecoUseCase(enderecoClient);

		EnderecoResultadoDto response = useCase.executar(new EnderecoRequestDto("00000-000"));

		assertThat(response.httpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
		assertThat(response.body().mensagem()).isEqualTo("Endereco nao encontrado para a localidade informada.");
	}

	@Test
	void deveRetornarErroInternoQuandoConsultaFalhar() {
		EnderecoClient enderecoClient = cep -> {
			throw new RestClientException("ViaCEP indisponivel");
		};
		BuscarEnderecoUseCase useCase = new BuscarEnderecoUseCase(enderecoClient);

		EnderecoResultadoDto response = useCase.executar(new EnderecoRequestDto("11691-024"));

		assertThat(response.httpStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
		assertThat(response.body().mensagem()).isEqualTo("Nao foi possivel consultar o endereco neste momento.");
	}
}
