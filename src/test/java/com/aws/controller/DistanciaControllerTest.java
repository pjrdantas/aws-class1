package com.aws.controller;

import com.aws.dto.DistanciaFormatadaResponseDto;
import com.aws.exception.CampoObrigatorioException;
import com.aws.usecase.CalcularDistanciaUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DistanciaController.class)
class DistanciaControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private CalcularDistanciaUseCase calcularDistanciaUseCase;

	@Test
	void deveRetornarDistanciaFormatada() throws Exception {
		when(calcularDistanciaUseCase.executar(any()))
				.thenReturn(new DistanciaFormatadaResponseDto("5km e 132m", "Distancia calculada com sucesso"));

		mockMvc.perform(post("/distancias/ceps")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "primeira": {
								    "cep": "11691024"
								  },
								  "segunda": {
								    "cep": "01001000"
								  }
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.distancia").value("5km e 132m"))
				.andExpect(jsonPath("$.mensagem").value("Distancia calculada com sucesso"));
	}

	@Test
	void deveRetornarErroPadronizadoQuandoCepForObrigatorio() throws Exception {
		when(calcularDistanciaUseCase.executar(any()))
				.thenThrow(new CampoObrigatorioException("O CEP da primeira localidade deve ser informado."));

		mockMvc.perform(post("/distancias/ceps")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "segunda": {
								    "cep": "01001000"
								  }
								}
								"""))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status").value(400))
				.andExpect(jsonPath("$.erro").value("Bad Request"))
				.andExpect(jsonPath("$.mensagem").value("O CEP da primeira localidade deve ser informado."));
	}
	@Test
	void deveAceitarEndpointAlternativoCepsCoordenadas() throws Exception {
		when(calcularDistanciaUseCase.executar(any()))
				.thenReturn(new DistanciaFormatadaResponseDto("5km e 132m", "Distancia calculada com sucesso"));

		mockMvc.perform(post("/ceps/coordenadas")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
							{
							  "primeira": {
							    "cep": "11691024"
							  },
							  "segunda": {
							    "cep": "01001000"
							  }
							}
							"""))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.distancia").value("5km e 132m"))
				.andExpect(jsonPath("$.mensagem").value("Distancia calculada com sucesso"));
	}

}
