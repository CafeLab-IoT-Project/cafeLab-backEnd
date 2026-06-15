# User Story: US21 - Consulta de historial ambiental por lote
Feature: Environmental History API
  Como consumidor del backend
  Quiero consultar el historial de lecturas por lote
  Para analizar la evolucion de temperatura y humedad durante el almacenamiento

  Scenario: Historial disponible para un lote monitoreado
    Given el lote con id 7 tiene lecturas historicas registradas
    When el cliente consulta el historial mediante la API de telemetria
    Then la API devuelve al menos una lectura con temperatura y humedad

  Scenario: Historial vacio para un lote sin lecturas
    Given el lote con id 99 no tiene lecturas registradas
    When el cliente consulta el historial mediante la API de telemetria
    Then la API responde con estado 200
    And la respuesta contiene una lista vacia
