# Origen y evolución de FuengiUMA

## Material consultado

- **Software presentación.pdf**, 17 diapositivas, aportada por Mario. Las diapositivas 3–7 describen servicio y requisitos; 8–15 contienen diagramas; la 16 enumera la implementación presentada y la 17 recoge dificultades.
- Copias de `g1-ingenieriaSoftware`, `g1-ingenieriaSoftware (mioMartes)` y la carpeta de entregas `fuengiUMA`.
- [Repositorio original](https://github.com/javidfdez/g1-ingenieriaSoftware), referencia local examinada `fa7e7eb`, del 4 de junio de 2022. El enlace aportado señala a `fuengiuma/src/main`.

Los documentos se han tratado como fuentes del proyecto. Los originales y su historial no se han modificado. Esta entrega no incluye bases históricas, carpetas del IDE, archivos de usuarios ni binarios antiguos.

## Proyecto original

FuengiUMA proponía compartir coche entre estudiantes para reducir coste y tiempo de desplazamiento. El análisis contemplaba cuentas, perfiles, viajes, plazas, solicitudes, mensajería y valoraciones.

La presentación identifica como implementados registro/inicio de sesión, listado de usuarios con acceso de administrador, creación y modificación de viajes, listado y detalle, con Java, Spring y HTML. Las copias utilizan Spring Boot 2.7.0, MVC/Thymeleaf, JPA y H2, con una variante del modelo de lugares. El flujo histórico de acceso y reservas es incompleto en el código revisado. No debe describirse como producto en producción ni como si todos sus requisitos hubieran quedado terminados entonces.

## Edición de 2026

| Área | Evidencia histórica | Edición actual |
|---|---|---|
| Análisis y UML | Casos de uso, secuencia y clases | Reglas reconstruidas y modelo persistente documentado |
| Usuarios | Formularios, entidad y flujo de registro/acceso | Sesiones, BCrypt, validación y permisos en servidor |
| Viajes | Crear, modificar, listar y consultar | API, filtros, publicación, edición y cancelación con autoría |
| Reservas | Requisitos y fragmentos incompletos | Estados y control concurrente de capacidad |
| Mensajería | Requisito del análisis | Chat por viaje para participantes con solicitud activa |
| Valoraciones | Requisito de 1–5 y comentarios | Por rol, ligadas a viajes confirmados y finalizados |
| Interfaz | Vistas HTML originales | Interfaz adaptable compartida por Java y demo estática |
| Entrega | No se presume cobertura histórica | Pruebas de integración/concurrencia/prototipo, flujo CI y documentación |

La edición actual reimplementa la aplicación; no es una exportación intacta de 2022. Conserva la propuesta y procedencia académica. Las decisiones nuevas incluyen chat colectivo por viaje con actualización manual, un campus y una localidad por trayecto, 1–4 plazas y aportaciones de hasta 25 € por persona. No se incluyen administración de usuarios, mapas/GPS ni cobros.

## Autoría y presentación profesional

El README histórico enumera siete personas, reproducidas en el README actual. Que una función aparezca en esta reconstrucción no demuestra quién la desarrolló en la asignatura. La edición de octubre de 2026 se ha elaborado con asistencia de IA a partir de los materiales aportados por Mario.

Para el CV pueden documentarse el proyecto colectivo, su objetivo, la tecnología original y los artefactos del análisis. La contribución individual de Mario debe concretarse con su explicación y evidencias. Las funciones añadidas pertenecen a esta reconstrucción y deben fecharse como tal.
