# User Story: US17 - Registro y Autenticacion
Feature: Authentication API
  Como consumidor del backend
  Quiero autenticarme mediante la API
  Para acceder a funcionalidades protegidas del sistema

  Scenario: Sign in exitoso con credenciales validas
    Given existe un usuario registrado con credenciales validas
    When el cliente envia una solicitud POST a "/api/v1/authentication/sign-in" con email y password correctos
    Then la API responde con estado 200
    And la respuesta incluye los datos del usuario autenticado y un token

  Scenario: Sign in fallido con solicitud invalida
    Given el cliente prepara una solicitud de autenticacion invalida
    When el cliente envia una solicitud POST a "/api/v1/authentication/sign-in" con un body mal formado o incompleto
    Then la API responde con estado 400 o el error correspondiente
    And la respuesta informa que la solicitud no pudo procesarse
