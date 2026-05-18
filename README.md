# aws-class1

API Spring Boot atuando como BFF orquestrador para calcular a distancia entre dois CEPs.

## Fluxo

1. Recebe dois CEPs.
2. Consulta, um CEP por vez, o servico de coordenadas:

```http
POST http://localhost:8081/ceps/coordenadas
```

3. Usa as coordenadas retornadas para consultar o servico de distancia:

```http
POST http://localhost:8082/distancia
```

4. Retorna a distancia formatada.

## Endpoint do BFF

```http
POST /distancias/ceps
Content-Type: application/json
```

## JSON de entrada

```json
{
  "primeira": {
    "cep": "11691024"
  },
  "segunda": {
    "cep": "01001000"
  }
}
```

## Consulta de coordenadas

Para cada CEP, o BFF chama:

```http
POST http://localhost:8081/ceps/coordenadas
Content-Type: application/json
```

Body:

```json
{
  "cep": "11691024"
}
```

Resposta esperada:

```json
{
  "cep": "11691-024",
  "rua": "Rua Magnolias",
  "bairro": "Mato Dentro",
  "cidade": "Ubatuba",
  "estado": "SP",
  "latitude": -23.4406838,
  "longitude": -45.0911815
}
```

## Consulta de distancia

Depois de consultar os dois CEPs, o BFF chama:

```http
POST http://localhost:8082/distancia
Content-Type: application/json
```

Body:

```json
{
  "pontoA": {
    "latitude": -23.4406838,
    "longitude": -45.0911815
  },
  "pontoB": {
    "latitude": -23.55052,
    "longitude": -46.633308
  }
}
```

Resposta esperada:

```json
{
  "distanciaMetros": 5132.0,
  "mensagem": "Distancia calculada com sucesso"
}
```

## Resposta de sucesso do BFF

HTTP `200 OK`

```json
{
  "distancia": "5km e 132m",
  "mensagem": "Distancia calculada com sucesso"
}
```

Se a distancia for menor que mil metros, o retorno fica em metros:

```json
{
  "distancia": "999m",
  "mensagem": "Distancia calculada com sucesso"
}
```


## Exemplo de requisicao e resposta

Exemplo de chamada:

```http
POST http://localhost:8080/ceps/coordenadas
Content-Type: application/json
```

Body:

```json
{
  "primeira": {
    "cep": "11691024"
  },
  "segunda": {
    "cep": "01001000"
  }
}
```

Resposta recebida:

```json
{
  "distancia": "157km e 708m",
  "mensagem": "Distancia calculada com sucesso"
}
```

## Respostas de erro

### CEP obrigatorio nao informado

HTTP `400 Bad Request`

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "O CEP da primeira localidade deve ser informado."
}
```

### JSON invalido

HTTP `400 Bad Request`

```json
{
  "status": 400,
  "erro": "Bad Request",
  "mensagem": "Requisicao invalida."
}
```

### Coordenadas nao encontradas

HTTP `404 Not Found`

```json
{
  "status": 404,
  "erro": "Not Found",
  "mensagem": "Coordenadas nao encontradas para um dos CEPs informados."
}
```

### Erro ao orquestrar chamadas

HTTP `500 Internal Server Error`

```json
{
  "status": 500,
  "erro": "Internal Server Error",
  "mensagem": "Nao foi possivel orquestrar o calculo de distancia neste momento."
}
```
