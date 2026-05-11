# User Stories: US10 - Control de Inventario Integrado, US13 - Gestion de Costos de Produccion
Feature: Inventory and costing API
  Como usuario autenticado
  Quiero registrar consumos y costos de produccion
  Para controlar inventario y evaluar rentabilidad

  Scenario: Registro exitoso de consumo de inventario
    Given el usuario autenticado tiene un lote de cafe disponible en su cuenta
    When el cliente envia una solicitud POST a "/api/v1/inventory-entries" con cantidad, fecha y producto final validos
    Then la API responde con estado 201
    And la respuesta devuelve la entrada de inventario creada

  Scenario: Registro exitoso de costos de produccion
    Given el usuario autenticado tiene un lote de cafe valido para costeo
    When el cliente envia una solicitud POST a "/api/v1/production-cost-records" con costos y moneda validos
    Then la API responde con estado 201
    And la respuesta devuelve el registro de costos calculado
