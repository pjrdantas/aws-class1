package com.aws.client;

import com.aws.dto.DistanciaRequestDto;
import com.aws.dto.DistanciaResponseDto;

public interface DistanciaClient {

	DistanciaResponseDto calcularDistancia(DistanciaRequestDto request);
}
