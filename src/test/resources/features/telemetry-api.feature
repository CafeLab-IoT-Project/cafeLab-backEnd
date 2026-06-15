# User Story: US20 - Visualizacion de condiciones del almacen en tiempo real
Feature: Telemetry Records API
  Como consumidor del backend
  Quiero registrar y consultar lecturas de telemetria
  Para visualizar las condiciones ambientales del almacen en tiempo real

  Scenario: Registro exitoso de una lectura de telemetria
    Given existe un lote de cafe valido asociado al dispositivo IoT
    When el cliente envia una solicitud POST a "/api/v1/telemetry-records" con temperatura, humedad y timestamp validos
    Then la API responde con estado 201
    And la respuesta incluye el identificador del registro y los valores capturados

  Scenario: Consulta del historial ambiental por lote
    Given existen lecturas de telemetria registradas para un lote
    When el cliente envia una solicitud GET a "/api/v1/telemetry-records/coffee-lot/{coffeeLotId}"
    Then la API responde con estado 200
    And la respuesta incluye la lista de lecturas ordenadas para el lote solicitado
