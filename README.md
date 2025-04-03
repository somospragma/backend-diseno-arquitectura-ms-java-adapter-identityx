# CO-JAVA-ADAPTER-IDENTITYX

Microservicio encargado de orquestar las operaciones del IdentityX REST API, aca encontramos operaciones para autenticacion y registro con FIDO, OTP.

## Requisitos
- **Java 17**
- **Gradle** (o usar el wrapper incluido)

## Instalación

Clona el repositorio y accede al directorio del proyecto:
```sh
git clone https://github.com/somospragma/co-java-adapter-identityx.git
cd co-java-adapter-identityx
```

## Construcción y Ejecución

Para compilar y ejecutar el proyecto, usa los siguientes comandos:

### Compilar el proyecto:
```sh
gradle build
```

### Ejecutar la aplicación:
```sh
gradle run
```

## API REST

El proyecto incluye una API REST documentada en `api-docs.yaml`. Puedes visualizar la documentación utilizando Swagger UI:

1. Levanta el servidor:
   ```sh
   gradle bootRun
   ```
2. Accede a `http://localhost:8080/swagger-ui.html` en tu navegador.

### Endpoints disponibles

#### `PUT /security/v1/secure_device/registrations/device/validate`
**Descripción**: Endpoint para actualizar un desafío de registro.

**Headers requeridos:**
- `channel-id` (string)
- `application` (string)
- `timestamp` (string)
- `transaction_id` (string)
- `terminal_id` (string)
- `token-client-id` (string, opcional)

#### `POST /security/v1/secure_device/authentication`
**Descripción**: Autentica un dispositivo seguro.

**Headers requeridos:**
- `channel-id` (string)
- `application` (string)
- `timestamp` (string)
- `transaction_id` (string)
- `terminal_id` (string)
- `token-client-id` (string, opcional)

**Body:**
```json
{
  "device_id": "string",
  "auth_data": "string"
}
```

#### `GET /security/v1/secure_device/status`
**Descripción**: Obtiene el estado del dispositivo seguro.

**Headers requeridos:**
- `channel-id` (string)
- `application` (string)
- `timestamp` (string)
- `transaction_id` (string)
- `terminal_id` (string)
- `token-client-id` (string, opcional)

#### `POST /security/v1/secure_device/registrations`
**Descripción**: Registra un nuevo dispositivo seguro.

**Headers requeridos:**
- `channel-id` (string)
- `application` (string)
- `timestamp` (string)
- `transaction_id` (string)
- `terminal_id` (string)
- `token-client-id` (string, opcional)

**Body:**
```json
{
  "device_id": "string",
  "registration_data": "string"
}
```

#### `DELETE /security/v1/secure_device/registrations/{device_id}`
**Descripción**: Elimina el registro de un dispositivo seguro.

**Headers requeridos:**
- `channel-id` (string)
- `application` (string)
- `timestamp` (string)
- `transaction_id` (string)
- `terminal_id` (string)
- `token-client-id` (string, opcional)

## Pruebas

Para ejecutar las pruebas unitarias:
```sh
gradle test
```

## Contribución

Si deseas contribuir, por favor sigue estos pasos:
1. Haz un fork del repositorio.
2. Crea una nueva rama (`git checkout -b feature-nueva-funcionalidad`).
3. Realiza tus cambios y haz commits (`git commit -m 'Descripción del cambio'`).
4. Sube tus cambios (`git push origin feature-nueva-funcionalidad`).
5. Abre un Pull Request.

## Licencia

Este proyecto está bajo la licencia MIT.
