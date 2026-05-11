# User Story: US01 - Registro de Proveedores
Feature: Supplier API
  Como usuario autenticado
  Quiero registrar y consultar proveedores
  Para gestionar mi red de abastecimiento

  Scenario: Creacion exitosa de proveedor con datos validos
    Given el usuario autenticado pertenece a un perfil valido
    When el cliente envia una solicitud POST a "/api/v1/suppliers" con nombre, email, telefono y ubicacion validos
    Then la API responde con estado 201
    And la respuesta devuelve el proveedor creado

  Scenario: Listado de proveedores del usuario autenticado
    Given existen proveedores registrados para el perfil autenticado
    When el cliente envia una solicitud GET a "/api/v1/suppliers"
    Then la API responde con estado 200
    And la respuesta contiene la lista de proveedores del usuario
