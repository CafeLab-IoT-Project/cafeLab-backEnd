# User Story: US23 - Configuracion de umbrales de monitoreo
Feature: Environment Thresholds API
  Como consumidor del backend
  Quiero configurar umbrales ambientales por lote
  Para evaluar si las condiciones del almacen se mantienen dentro de rangos seguros

  Scenario: Creacion exitosa de umbrales para un lote
    Given el lote con id 4 no tiene umbrales configurados
    When el cliente envia una solicitud POST a "/api/v1/environment-thresholds" con rangos validos
    Then la API responde con estado 201
    And la respuesta incluye los valores minimos y maximos configurados

  Scenario: Consulta de umbrales existentes por lote
    Given el lote con id 8 tiene umbrales configurados
    When el cliente envia una solicitud GET a "/api/v1/environment-thresholds/coffee-lot/8"
    Then la API responde con estado 200
    And la respuesta incluye la configuracion de temperatura y humedad del lote

  Scenario: Actualizacion de umbrales existentes
    Given el lote con id 6 tiene umbrales configurados
    When el cliente envia una solicitud PUT a "/api/v1/environment-thresholds/coffee-lot/6" con nuevos rangos
    Then la API responde con estado 200
    And la respuesta refleja los nuevos valores configurados

  Scenario: Consulta de umbrales inexistentes
    Given el lote con id 404 no tiene umbrales configurados
    When el cliente consulta los umbrales del lote
    Then la API responde con estado 404
    And la respuesta informa que no existe configuracion para el lote
