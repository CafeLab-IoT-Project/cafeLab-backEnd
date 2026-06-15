# User Story: US24 - Indicador de estado ambiental por lote
Feature: Environmental Status Evaluation API
  Como consumidor del backend
  Quiero disponer de lecturas y umbrales consistentes por lote
  Para determinar el estado ambiental del lote en la capa de presentacion

  Scenario: Evaluacion de estado con lectura dentro de rango
    Given el lote tiene umbrales configurados entre 18 y 24 grados y entre 45 y 60 por ciento de humedad
    And la ultima lectura reporta 20.5 grados y 58 por ciento de humedad
    When el cliente consulta lecturas y umbrales del lote
    Then la API entrega datos suficientes para representar un estado ambiental optimo

  Scenario: Evaluacion de estado con lectura fuera de rango
    Given el lote tiene umbrales configurados entre 18 y 24 grados
    And la ultima lectura reporta 27 grados
    When el cliente consulta lecturas y umbrales del lote
    Then la API entrega datos suficientes para representar un estado ambiental critico
