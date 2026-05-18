package com.aws.client;

import com.aws.dto.CepRequestDto;
import com.aws.dto.CoordenadasResponseDto;

public interface CoordenadasClient {

	CoordenadasResponseDto buscarCoordenadas(CepRequestDto request);
}
