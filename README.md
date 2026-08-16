# Aplicación Demo para pipeline de CI/CD

Aplicación sencilla desarrollada con Spring Boot 4 que expone varios endpoints HTTP y una batería básica de tests automatizados.

---

## Requisitos

- Java 21
- Maven 3.9+ (o utilizar el Maven Wrapper incluido)

---

## Ejecución de la aplicación

Desde la raíz del proyecto:

```bash
./mvnw spring-boot:run
```

Una vez iniciada la aplicación, estará disponible en:

```text
http://localhost:8080
```

---

## Endpoints disponibles

### 1. Estado de salud

Permite comprobar que la aplicación está funcionando correctamente.

```http
GET /health
```

#### Ejemplo

http://localhost:8080/health

```json
{
  "status": "UP"
}
```

---

### 2. Saludo personalizado

Devuelve un mensaje de bienvenida utilizando el nombre recibido por parámetro.

```http
GET /greet?name=Daniel
```

#### Ejemplo

http://localhost:8080/greet?name=Daniel

```json
{
  "message": "Hola Daniel"
}
```

Si no se proporciona el parámetro `name`, se utilizará el valor por defecto:

```json
{
  "message": "Hola Mundo"
}
```

---

### 3. Operación de suma

Recibe dos números y devuelve el resultado de la suma.

```http
GET /sum?a=10&b=15
```

#### Ejemplo

http://localhost:8080/sum?a=10&b=15

```json
{
  "result": 25
}
```

---

## Tests automatizados

El proyecto incluye una batería de tests para validar el comportamiento de los endpoints REST.

Los tests utilizan:

- JUnit 5
- Spring Boot Test
- MockMvc

---

### Casos cubiertos

Actualmente se validan los siguientes escenarios:

#### 1. Health Endpoint

Verifica que:

- El endpoint responde con código HTTP 200.
- El campo `status` contiene el valor `UP`.

---

#### 2. Greeting Endpoint

Verifica que:

- Se devuelve el saludo correcto para un nombre recibido por parámetro.
- Se utiliza correctamente el valor por defecto cuando no se especifica ningún nombre.

---

#### 3. Sum Endpoint

Verifica que:

- El cálculo de la suma es correcto.
- La respuesta contiene el valor esperado.

---

### Ejecución de los tests

Para ejecutar todos los tests del proyecto:

```bash
./mvnw test
```


Al finalizar se mostrará un resumen similar a:

```text
Tests run: 4
Failures: 0
Errors: 0
Skipped: 0
```

---

## Construcción del proyecto

Para ejecutar los tests y generar el artefacto JAR:

```bash
./mvnw clean package
```

El fichero generado quedará disponible en:

```text
target/
```

Este comando será especialmente útil para integrarlo posteriormente en un pipeline de GitHub Actions.

## Flujo de Integración Continua (CI)

Este proyecto utiliza GitHub Actions para ejecutar automáticamente los tests unitarios cada vez que se realizan cambios en el código fuente.

- Garantiza que los cambios introducidos en el repositorio no rompan funcionalidades existentes.
- Antes de aprobar o integrar un cambio, GitHub ejecutará de forma automática la batería de pruebas definida en el proyecto.

---

### Ubicación del workflow

El workflow se encuentra en:

```text
.github/workflows/ci.yml
```

### Cuándo se ejecuta

El pipeline se ejecuta automáticamente cuando ocurre alguno de los siguientes eventos en la rama **main**:

- Push
- Pull Request

### Qué ocurre durante la ejecución

#### 1. Descarga del código

GitHub crea una máquina virtual temporal y descarga el contenido del repositorio.

#### 2. Instalación de Java

Se instala Java 21 utilizando la distribución Temurin.

#### 3. Restauración de dependencias Maven

Las dependencias descargadas previamente pueden recuperarse desde caché para acelerar la ejecución.

#### 4. Ejecución de tests

Se ejecuta:

```bash
./mvnw test
```

Durante este paso se lanzan todos los tests JUnit y MockMvc incluidos en el proyecto.

Actualmente se validan:

- Endpoint `/health`
- Endpoint `/greet`
- Endpoint `/sum`

#### 5. Publicación de informes

Los resultados de los tests se almacenan como artefactos del workflow y pueden descargarse desde GitHub.

---

### Ejemplo de flujo de trabajo en Github

```text
Push / Pull Request recibido
        ↓
Inicia GitHub Actions
        ↓
Instala Java
        ↓
Análisis estático
        ↓
Ejecuta Maven Test
        ↓
Publica resultados
        ↓
Marca la ejecución como OK ✅ o ERROR ❌
```

---

## Flujo de Entrega Continua (CD)

Este proyecto utiliza Jenkins para automatizar el proceso de construcción y publicación de imágenes Docker en GitHub Container Registry.

---

### Jenkinsfile

La definición de la pipeline se encuentra en el archivo:

```text
Jenkinsfile
```

---

### Etapas de la pipeline

#### 1. Checkout

Obtiene el código fuente desde GitHub.

```groovy
git 'https://github.com/danielcalderon/proyecto-devops.git'
```

#### 2. Prepare

Concede permisos de ejecución al Maven Wrapper.

```groovy
chmod +x mvnw
```

#### 3. Build

Compila la aplicación y genera el artefacto JAR.

```groovy
./mvnw clean package
```

#### 4. Test

Ejecuta la batería de pruebas automatizadas.

```groovy
./mvnw test
```

#### 5. Docker Build

Construye la imagen Docker de la aplicación.

```groovy
docker build -t ${IMAGE_NAME}:latest .
```

Utiliza el Dockerfile ubicado en la raíz del proyecto.

#### 6. Docker Push

Publica la imagen en GitHub Container Registry.

---

### Ejemplo de ejecución completa

```text
Developer
    ↓
Push a GitHub
    ↓
Jenkins Pipeline
    ↓
Checkout
    ↓
Build
    ↓
Tests
    ↓
Docker Build
    ↓
Docker Push
    ↓
Imagen publicada en GHCR
```

---

### Resultado esperado

Si todas las etapas se completan correctamente:

```text
✅ Pipeline successful
```

y la imagen queda publicada en:

```text
ghcr.io/danielcalderon/proyecto-devops:latest
```
