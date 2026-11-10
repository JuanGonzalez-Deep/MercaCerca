# MercaCerca – Frontend Android (Actividad 3)

Tienda virtual de productos locales. Esta etapa entrega **solo las interfaces** (XML + navegación + validaciones de formulario); los datos son de demostración (`Data.kt`) y la conexión con las APIs de autenticación, almacenamiento y pagos queda para la siguiente actividad.

## Pantallas por rol
| Rol | Navegación | Pantallas |
|---|---|---|
| Transversal | – | Splash, Login con biometría (huella), Recuperar contraseña |
| Administrador | Navigation Drawer | Dashboard, Usuarios (listar/crear/editar/eliminar), Productos, Reporte de ventas |
| Vendedor | Bottom Navigation | Mis productos (CRUD + cámara), Pedidos recibidos, Perfil |
| Comprador | Bottom Navigation | Inicio, Catálogo (búsqueda + filtro), Detalle de producto, Carrito, Pasarela de pagos, Perfil |

## Recursos personalizados (nada por defecto)
- `values/colors.xml`: paleta verde petróleo `#1B5E4B` + ámbar `#F29F05` + crema `#F7F3EC`.
- `values/styles.xml`: tema propio, estilos de botón, input, tarjeta, chip, badge y tipografías.
- `values/dimens.xml`, `values/strings.xml` (todo en español).
- `drawable/`: degradados, formas, íconos vectoriales propios, ícono de la app.
- `color/`: selectores de estado para navegación y chips.
- `menu/`: menú del Drawer y de los dos Bottom Navigation.

## Cómo abrir y ejecutar
1. Android Studio Hedgehog o superior (JDK 17).
2. File > Open > carpeta `MercaCerca`. Espera el Gradle Sync.
3. Elige un emulador/dispositivo (API 26+) y pulsa Run.
4. En el login escoge el rol con los chips (Comprador / Vendedor / Administrador) e ingresa cualquier correo válido y contraseña de 6+ caracteres.

## Generar el APK
Build > Build Bundle(s) / APK(s) > Build APK(s). Queda en `app/build/outputs/apk/debug/app-debug.apk`.

## Git y GitHub
```bash
git init
git add .
git commit -m "feat: frontend completo MercaCerca (Actividad 3)"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/MercaCerca.git
git push -u origin main
```
Repositorio **público** (Settings > Danger Zone > Change visibility).
