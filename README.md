# Management Project

Proyecto Spring Boot con arquitectura hexagonal (Ports and Adapters) usando Gradle.

## Estructura del Proyecto

```
src/main/java/com/example/management/
├── application/                # Capa de Aplicación
│   ├── ports/                 # Puertos (Interfaces)
│   │   ├── in/                # Puertos de Entrada (Interfaces de Casos de Uso)
│   │   └── out/               # Puertos de Salida (Interfaces de Repositorio/Externas)
│   └── services/              # Implementaciones de Casos de Uso
├── domain/                     # Capa de Dominio (Lógica Pura)
│   ├── model/                 # Entidades/VOs del Dominio
│   └── exception/             # Excepciones del Dominio
├── infrastructure/             # Capa de Infraestructura (Adaptadores)
│   ├── adapters/
│   │   ├── in/                # Adaptadores de Entrada (Web/REST/Controllers)
│   │   └── out/               # Adaptadores de Salida (Persistencia/APIs Externas)
│   └── config/                # Configuración específica del framework
└── ManagementApplication.java  # Punto de Entrada Principal
```

## Requisitos

- Java 17+
- Gradle 8.0+

## Ejecución

```bash
./gradlew bootRun
```

## Construcción

```bash
./gradlew build
```

## Pruebas

```bash
./gradlew test
```
