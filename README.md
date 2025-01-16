# Sistema de Auditoría de Transacciones Bancarias

Este proyecto es una aplicación web que gestiona y audita las transacciones bancarias utilizando **Spring WebFlux** para crear un sistema reactivo. Permite realizar depósitos, retiros y consultar las transacciones de un usuario, todo ello con un registro de auditoría detallado en una base de datos MongoDB.

## Características

- **Depósitos**: Realiza depósitos en la cuenta de un usuario y guarda la auditoría de la transacción.
- **Retiros**: Realiza retiros de la cuenta de un usuario, validando el saldo disponible, y guarda la auditoría.
- **Consulta de transacciones**: Permite consultar todas las transacciones de un usuario, incluidas las realizadas por depósito y retiro.

## Tecnologías Utilizadas

- **Java** (Spring Boot)
- **Spring WebFlux**: Para crear una API reactiva.
- **MongoDB**: Para almacenar los registros de auditoría.
- **JUnit 5 & Mockito**: Para pruebas unitarias y de integración.
- **WebTestClient**: Para realizar pruebas de los endpoints de la API.
- **Lombok**: Para simplificar el código con anotaciones de generación automática de getters, setters, etc.

## Endpoints

### `GET /api/transactions/{userId}`

Obtiene las transacciones de un usuario específico.

**Parámetros:**
- `userId` (Path variable): El ID del usuario.

**Respuesta:**
- Lista de transacciones del usuario con detalles como tipo de transacción, monto y saldo final.

### `POST /api/transactions/deposit`

Realiza un depósito en la cuenta de un usuario.

**Parámetros:**
- `userId` (Query parameter): El ID del usuario.
- `amount` (Query parameter): El monto del depósito.

**Respuesta:**
- Transacción de auditoría que contiene detalles del depósito realizado.

### `POST /api/transactions/withdraw`

Realiza un retiro de la cuenta de un usuario.

**Parámetros:**
- `userId` (Query parameter): El ID del usuario.
- `amount` (Query parameter): El monto a retirar.
- `withdrawalType` (Query parameter): El tipo de retiro (por ejemplo, "ATM").

**Respuesta:**
- Transacción de auditoría que contiene detalles del retiro realizado.

## Instalación

### Requisitos previos

- **Java 11 o superior**
- **Maven** o **Gradle**
- **MongoDB** (local o en la nube)

### Pasos para ejecutar el proyecto

1. Clona el repositorio:

   ```bash
   git clone https://github.com/tuusuario/tu-repositorio.git

2. Navega a la carpeta del proyecto:

    ```bash
    cd tu-repositorio
    ```

3. Compila el proyecto usando Gradle:

    ```bash
    ./gradlew build
    ```

4. Ejecuta la aplicación:

    ```bash
    ./gradlew bootRun
    ```

## Pruebas

Este proyecto incluye pruebas unitarias y de integración utilizando **JUnit 5** y **Mockito**. Las pruebas están localizadas en el paquete `src/test/java`.

Para ejecutar las pruebas, usa el siguiente comando con Gradle:

```bash
./gradlew test
