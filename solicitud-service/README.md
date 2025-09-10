# Servicio de Solicitudes de Crédito

## Descripción
Servicio reactivo para la gestión de solicitudes de crédito, implementado con Spring WebFlux y R2DBC para PostgreSQL. El sistema permite crear solicitudes de crédito con validaciones automáticas y manejo de estados, siguiendo los principios de Clean Architecture.

## Características Principales

✅ **Endpoint POST `/api/v1/solicitud`** - Creación de solicitudes de crédito  
✅ **Validaciones automáticas** - Email, monto, plazo y tipo de préstamo  
✅ **Estado automático** - Se asigna "Pendiente de revisión" por defecto  
✅ **Validación de tipos de préstamo** - Verifica existencia y rangos de monto  
✅ **Programación reactiva** - WebFlux y R2DBC para mejor rendimiento  
✅ **Logs de traza** - Monitoreo completo de operaciones  
✅ **Manejo de excepciones** - Respuestas de error consistentes y amigables  
✅ **Arquitectura hexagonal** - Separación clara de responsabilidades  
✅ **Modelos de dominio ricos** - Con lógica de negocio encapsulada  
✅ **Validaciones separadas** - En la capa de infraestructura, no en el dominio  

## Arquitectura Clean Code

El proyecto sigue estrictamente los principios de **Clean Architecture**:

### 🏗️ **Domain Layer (Capa más interna)**
- **Modelos de dominio ricos**: `Solicitud`, `TipoPrestamo`, `Estado`
- **Value Objects**: `Email`, `Monto`, `Plazo`, `SolicitudId`
- **Lógica de negocio**: Métodos como `calcularCuotaMensual()`, `esMontoValidoParaTipo()`
- **Casos de uso**: `SolicitudUseCase` con reglas de negocio puras

### 🔌 **Infrastructure Layer (Capa externa)**
- **Validadores**: `SolicitudValidator` para validaciones de entrada
- **Adaptadores**: Implementaciones de repositorios
- **Entry Points**: Handlers y routers de la API

### 📋 **Separación de Responsabilidades**
- **Dominio**: Solo lógica de negocio y reglas
- **Infraestructura**: Validaciones de entrada, persistencia, API
- **Aplicación**: Orquestación y configuración

## Estructura del Proyecto

```
solicitud-service/
├── domain/                          # Capa de dominio (CORE)
│   ├── model/                       # Modelos de negocio
│   │   └── solicitud/
│   │       ├── Solicitud.java       # Entidad principal con lógica de negocio
│   │       ├── SolicitudRequest.java # DTO de entrada
│   │       ├── SolicitudResponse.java # DTO de respuesta
│   │       ├── TipoPrestamo.java    # Tipo de préstamo
│   │       ├── Estado.java          # Estados de solicitud
│   │       ├── Email.java           # Value Object para email
│   │       ├── Monto.java           # Value Object para monto
│   │       ├── Plazo.java           # Value Object para plazo
│   │       ├── SolicitudId.java     # Value Object para ID
│   │       └── gateways/            # Interfaces de repositorio
│   └── usecase/                     # Casos de uso
│       └── solicitud/
│           └── SolicitudUseCase.java # Lógica de negocio pura
├── infrastructure/                   # Capa de infraestructura
│   ├── driven-adapters/             # Adaptadores de base de datos
│   │   └── r2dbc-postgresql/        # Implementación R2DBC
│   │       ├── SolicitudRepositoryAdapter.java
│   │       ├── TipoPrestamoRepositoryAdapter.java
│   │       └── EstadoRepositoryAdapter.java
│   └── entry-points/                # Puntos de entrada
│       └── reactive-web/            # API WebFlux
│           ├── Handler.java         # Manejador de requests
│           ├── RouterRest.java      # Configuración de rutas
│           └── validator/           # Validadores de entrada
│               └── SolicitudValidator.java
├── applications/                     # Configuración de aplicación
│   └── app-service/
│       ├── ObjectMapperConfig.java  # Configuración JSON
│       └── UseCasesConfig.java      # Configuración de casos de uso
└── deployment/                      # Scripts de despliegue
    ├── Dockerfile                   # Contenedor Docker
    └── init-data.sql               # Datos iniciales
```

## Modelos de Dominio

### 🎯 **Solicitud (Entidad Principal)**
```java
public class Solicitud {
    private SolicitudId idSolicitud;
    private Monto monto;
    private Plazo plazo;
    private Email email;
    private Estado estado;
    private TipoPrestamo tipoPrestamo;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaActualizacion;
    
    // Métodos de negocio
    public boolean puedeSerAprobada()
    public boolean esMontoValidoParaTipo()
    public BigDecimal calcularCuotaMensual()
    public void asignarEstado(Estado nuevoEstado)
    public void marcarComoCreada()
}
```

### 🔢 **Value Objects**
- **Email**: Validación de formato y estructura
- **Monto**: Validación de rango y operaciones matemáticas
- **Plazo**: Validación de límites (1-360 meses)
- **SolicitudId**: Identificador único de la solicitud

## Flujo de Validación

```
Request → Handler → Validator → UseCase → Repository
   ↓         ↓         ↓         ↓         ↓
JSON    Parse    Validar    Lógica    Persistir
        JSON     Entrada    Negocio   Datos
```

### ✅ **Validaciones por Capa**
1. **Infraestructura**: Formato JSON, tipos de datos, rangos básicos
2. **Dominio**: Reglas de negocio, validaciones de negocio
3. **Base de Datos**: Constraints y validaciones de integridad

## Requisitos Previos

- Java 19+
- PostgreSQL 12+
- Gradle 8.14.3+

## Instalación

### 1. Base de Datos
```sql
-- Ejecutar el script de creación de tablas proporcionado
-- Luego ejecutar el script de datos iniciales:
\i deployment/init-data.sql
```

### 2. Configuración
Asegúrate de que el archivo `application.yaml` tenga la configuración correcta de la base de datos.

### 3. Compilación
```bash
./gradlew clean build
```

### 4. Ejecución
```bash
./gradlew bootRun
```

## Uso de la API

### Crear Solicitud de Crédito

**Endpoint:** `POST /api/v1/solicitud`

**Request:**
```json
{
  "email": "cliente@ejemplo.com",
  "monto": 5000000.00,
  "plazo": 24,
  "idTipoPrestamo": 1
}
```

**Response de Éxito:**
```json
{
  "success": true,
  "message": "Solicitud creada exitosamente",
  "data": {
    "idSolicitud": 1,
    "monto": 5000000.00,
    "plazo": 24,
    "email": "cliente@ejemplo.com",
    "estado": "Pendiente de revisión",
    "tipoPrestamo": "Préstamo Personal",
    "fechaCreacion": "2024-01-15T10:30:00",
    "mensaje": "Solicitud creada exitosamente"
  }
}
```

### Tipos de Préstamo Disponibles

| ID | Nombre | Monto Mínimo | Monto Máximo | Tasa |
|----|--------|--------------|--------------|------|
| 1  | Préstamo Personal | $1,000,000 | $50,000,000 | 2.50% |
| 2  | Préstamo Hipotecario | $50,000,000 | $500,000,000 | 1.80% |
| 3  | Préstamo Vehicular | $5,000,000 | $100,000,000 | 2.20% |
| 4  | Préstamo de Libre Inversión | $2,000,000 | $100,000,000 | 3.00% |
| 5  | Microcrédito | $100,000 | $5,000,000 | 4.50% |

## Validaciones Implementadas

### ✅ **Validaciones de Entrada (Infraestructura)**
- Email obligatorio y formato válido
- Monto mayor a 0 y dentro de límites
- Plazo entre 1 y 360 meses
- Tipo de préstamo obligatorio

### ✅ **Validaciones de Negocio (Dominio)**
- Verificación de existencia del tipo de préstamo
- Validación de rangos de monto por tipo
- Asignación automática de estado "Pendiente de revisión"
- Registro automático de fechas
- Cálculo de cuota mensual

## Manejo de Errores

La API maneja todos los errores de forma consistente:

- **400 Bad Request**: Datos inválidos o formato incorrecto
- **500 Internal Server Error**: Errores del sistema

Todos los errores incluyen:
- Mensaje descriptivo del problema
- Detalles específicos del error
- Formato JSON consistente

## Logs y Monitoreo

El sistema incluye logs detallados para:
- 📝 Recepción de solicitudes
- ✅ Validaciones exitosas
- ❌ Errores y excepciones
- 💾 Operaciones de base de datos
- 🎯 Creación exitosa de solicitudes

## Tecnologías Utilizadas

- **Spring WebFlux** - Framework reactivo
- **R2DBC** - Acceso reactivo a base de datos
- **PostgreSQL** - Base de datos
- **Reactor** - Programación reactiva
- **Lombok** - Reducción de código
- **Jackson** - Manejo de JSON
- **Gradle** - Gestión de dependencias

## Principios de Clean Architecture Aplicados

### 🎯 **Dependency Inversion**
- El dominio no depende de la infraestructura
- Las interfaces están en el dominio, implementaciones en infraestructura

### 🔒 **Single Responsibility**
- Cada clase tiene una responsabilidad específica
- Validadores solo validan, casos de uso solo implementan lógica de negocio

### 🚫 **Open/Closed Principle**
- Fácil extender funcionalidad sin modificar código existente
- Nuevos tipos de préstamo se agregan sin cambiar la lógica principal

### 🔄 **Dependency Injection**
- Todas las dependencias se inyectan, no se crean internamente
- Fácil testing y mantenimiento

## Pruebas

```bash
# Ejecutar todas las pruebas
./gradlew test

# Ejecutar pruebas con cobertura
./gradlew jacocoTestReport

# Ejecutar pruebas de mutación
./gradlew pitest
```

## Despliegue

### Docker
```bash
# Construir imagen
docker build -t solicitud-service .

# Ejecutar contenedor
docker run -p 8080:8080 solicitud-service
```

### Local
```bash
./gradlew bootRun
```

## Contribución

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Soporte

Para soporte técnico o preguntas, por favor contacta al equipo de desarrollo.

---

**Desarrollado con ❤️ usando Spring WebFlux, R2DBC y Clean Architecture**
