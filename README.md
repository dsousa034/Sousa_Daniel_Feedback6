# Feedback3

## Descripción
Este proyecto es una aplicación Android desarrollada en Kotlin que permite a los usuarios agregar, visualizar y gestionar novelas. Utiliza Firebase Firestore para el almacenamiento de datos y está construido con Jetpack Compose para la interfaz de usuario.

## Bitácora de Desarrollo

### Configuración del Proyecto
- **Tareas Realizadas:**
  - Creación del proyecto en Android Studio.
  - Configuración inicial de `build.gradle` y `settings.gradle`.
  - Integración de Firebase en el proyecto.
  - Descarga y colocación del archivo `google-services.json`.

### Implementación de la Interfaz de Usuario
- **Tareas Realizadas:**
  - Creación de la estructura básica de la aplicación utilizando Jetpack Compose.
  - Implementación de la pantalla principal (`MainScreen`).
  - Implementación de la pantalla para agregar novelas (`AgregarNovela`).

### Integración con Firebase Firestore
- **Tareas Realizadas:**
  - Configuración de las dependencias de Firebase Firestore en `build.gradle`.
  - Implementación de la lógica para agregar novelas a Firestore.
  - Pruebas iniciales de la integración con Firestore.

### Mejoras en la Interfaz de Usuario
- **Tareas Realizadas:**
  - Ajustes en el diseño de la pantalla `AgregarNovela`.
  - Definición de colores en `Theme.kt` y aplicación de estos en la interfaz.
  - Implementación de vistas previas (`@Preview`) para componentes de Compose.

### Corrección de Errores y Optimización
- **Tareas Realizadas:**
  - Solución de errores de compilación relacionados con referencias no resueltas.
  - Verificación de la correcta inicialización de Firebase en `MainActivity`.
  - Optimización del código y limpieza de dependencias no utilizadas.
