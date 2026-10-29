# Validación de la entrega

Fecha: **29 de octubre de 2026**. Windows, Java 17, Maven y Chrome.

| Comprobación ejecutada | Resultado |
|---|---|
| Compilación y empaquetado Maven | JAR ejecutable generado |
| 9 pruebas JUnit/MockMvc con H2 | 9 correctas, sin errores ni omisiones |
| 8 pruebas del prototipo con Node | 8 correctas |
| Arranque del JAR y `/api/bootstrap` | HTTP 200 |
| Sincronización de fuentes y `docs/` | Verificada con `--check` |
| Flujo Java en navegador | Acceso demo, búsqueda, solicitud, aceptación y mensaje |
| Flujo del prototipo en navegador | Acceso demo, publicación, edición y valoración |
| Vista de móvil | 390 × 844; sin desbordamiento horizontal en las vistas comprobadas |
| Consola en esos recorridos | Sin errores ni advertencias registrados |

Java comprueba credenciales, hash, CSRF, sesión, filtros, propiedad, duplicados, disponibilidad, acceso y revocación del chat, valoraciones y cancelación. La prueba de concurrencia lanza dos aceptaciones contra un viaje de una plaza y comprueba que solo una puede confirmarse.

El prototipo comprueba filtros, permisos, solicitud/chat/cancelación, capacidad, valoraciones, persistencia sin contraseñas en claro, rutas y recuperación ante almacenamiento malformado. Las capturas de `assets/screenshots/` proceden de las pantallas revisadas; los datos son ficticios.

Los resultados anteriores corresponden a las comprobaciones locales. El estado de la publicación y las pruebas remotas se consulta en Pages y Actions del repositorio. El Dockerfile no se ha ejecutado en Docker. La demo estática se ha probado mediante HTTP local; el lanzamiento directo como archivo depende del navegador. No se ha realizado una auditoría externa de seguridad, carga ni accesibilidad.
