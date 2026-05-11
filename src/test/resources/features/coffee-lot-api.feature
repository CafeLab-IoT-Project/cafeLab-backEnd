# User Story: US02 - Gestion de Lotes de Cafe Verde
Feature: Coffee lot API
  Como usuario autenticado
  Quiero registrar y consultar lotes de cafe verde
  Para administrar el inventario base de produccion

  Scenario: Creacion exitosa de lote con proveedor propio
    Given el usuario autenticado tiene un proveedor asociado a su cuenta
    When el cliente envia una solicitud POST a "/api/v1/coffee-lots" con datos validos del lote
    Then la API responde con estado 201
    And la respuesta devuelve el lote creado

  Scenario: Listado de lotes del usuario autenticado
    Given existen lotes de cafe registrados para el perfil autenticado
    When el cliente envia una solicitud GET a "/api/v1/coffee-lots"
    Then la API responde con estado 200
    And la respuesta contiene la lista de lotes del usuario
