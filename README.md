# A-Store Anáhuac Cancún

Página web de una tienda universitaria de demostración, hecha con **Java** (servidor y clases), **HTML, CSS y JavaScript** (página).

> **Aviso:** es un proyecto académico. No es un sitio oficial de la Universidad Anáhuac Cancún y no usa logotipos ni materiales institucionales. Los productos, precios e imágenes son ficticios y no se realiza ningún cobro real.

## Objetivo académico

Proyecto de la materia **Lenguajes Orientados a Objetos**. Sirve para aprender Java, programación orientada a objetos (clases, encapsulamiento, herencia, polimorfismo, abstracción, interfaces, excepciones y `ArrayList`) y trabajo en equipo con Git y GitHub (ramas y Pull Requests).

## Integrantes

- José Parra
- Paul Canto

## Tecnologías

| Parte | Tecnología |
|---|---|
| Lógica y servidor | Java. El servidor es `com.sun.net.httpserver`, que ya viene dentro del JDK (no se instala nada extra) |
| Página web | HTML, CSS y JavaScript sin librerías |
| Datos | Objetos en `ArrayList` + archivos CSV en la carpeta `datos/` (**no hay base de datos**) |
| Carrito en el navegador | `localStorage` |
| Control de versiones | Git y GitHub |

## Requisitos para ejecutar

- **JDK** (el que trae `javac` y `java`). El proyecto se probó con **JDK 21**; no se verificó con otras versiones.
- Un navegador web.
- No hace falta Maven, Gradle, internet ni base de datos.

Para comprobar que tienes el JDK, abre una terminal y escribe `javac -version`.

## Instalación

```bash
git clone https://github.com/JoseParra123/JoseParra-PaulCanto-AStore.git
cd JoseParra-PaulCanto-AStore
```

## Ejecución

**Windows:** doble clic en `ejecutar.bat` (o ejecútalo desde la terminal). *Este script no se pudo probar en Windows; si falla, usa los comandos de la sección "A mano".*

**Linux / Mac:**

```bash
./ejecutar.sh
```

Después abre **http://localhost:8080** en el navegador. Para detener el servidor presiona `Ctrl + C`.

Otras opciones:

| Comando | Qué hace |
|---|---|
| `./ejecutar.sh 9090` | Inicia la página en otro puerto (útil si el 8080 está ocupado) |
| `./ejecutar.sh demo` | Demostración en consola: ventas, pagos, ticket, corte de caja y factura |

**A mano** (funciona igual en cualquier sistema, desde la carpeta del proyecto):

```bash
javac -encoding UTF-8 -sourcepath src -d out src/astore/Main.java
java -cp out astore.Main
```

### Pruebas

Se ejecutan **desde una carpeta vacía** (para no mezclar los datos de prueba con los tuyos). Desde la carpeta del proyecto:

```bash
javac -encoding UTF-8 -sourcepath src -d out src/astore/Main.java
javac -encoding UTF-8 -cp out -d out pruebas/PruebasClases.java
mkdir /tmp/prueba && cd /tmp/prueba
java -cp /ruta/al/proyecto/out PruebasClases     # debe decir: fallidas: 0
node /ruta/al/proyecto/pruebas/carrito.test.js    # cálculos del carrito en JavaScript (requiere Node)
```

## Estructura de carpetas

```
.
├── src/astore/
│   ├── Main.java            Punto de entrada
│   ├── Tienda.java          Centro del sistema (listas y operaciones principales)
│   ├── util/                Guardable (interfaz) y GestorUsuario (guarda/lee CSV)
│   ├── productos/           Categoria, Producto, Inventario, Almacen, Proveedor, Promocion
│   ├── personas/            Persona, Cliente, Personal, Administrador
│   ├── compras/             ItemCarrito, Carrito, DetallePedido, Pedido, MetodoPago,
│   │                        PagoEfectivo, PagoTarjeta, PagoCredencial, Venta,
│   │                        Terminal, CierreCaja, Factura
│   └── web/                 ServidorWeb (API y archivos de la página)
├── web/                     Página: HTML, css/, js/ e img/ (imágenes de demostración)
├── pruebas/                 PruebasClases.java y carrito.test.js
├── documentacion/           Excel de clases y guía para exponer
├── datos/                   (lo crea el programa; no se sube a GitHub)
├── ejecutar.sh / ejecutar.bat
└── README.md
```

## Funcionalidades

- **Inicio:** presentación, categorías y productos destacados.
- **Catálogo:** filtro por categoría, búsqueda por texto y opción "solo disponibles"; cada producto muestra imagen, descripción, precio y disponibilidad (disponible, quedan pocas piezas o agotado).
- **Carrito:** agregar, eliminar, cambiar cantidades, subtotal, descuento y total. No deja pedir más piezas de las que hay.
- **Promociones:** códigos de ejemplo `BIENVENIDA10` (10 %, para todos) y `LEONES15` (15 %, solo estudiantes).
- **Pedidos:** al confirmar, el servidor revisa existencias, descuenta el inventario y guarda el pedido. Si algo falla, no se cambia nada.
- **Administración** (`/admin.html`): cambiar precio y existencias de cada producto y ver los pedidos recibidos.
- **Modo consola** (`demo`): ventas de mostrador con efectivo, tarjeta o crédito de credencial; ticket; corte de caja; factura.

## Descripción de las clases

| Paquete | Clase | Para qué sirve |
|---|---|---|
| (raíz) | `Main` | Inicia la página web o la demostración en consola |
| (raíz) | `Tienda` | Guarda las listas (`ArrayList`) y coordina pedidos, ventas y promociones |
| `util` | `Guardable` | Interfaz: cada clase dice en qué CSV se guarda y con qué columnas |
| `util` | `GestorUsuario` | Guarda y lee los archivos CSV y escribe el historial |
| `productos` | `Categoria` | Grupo de productos (Ropa, Papelería…) |
| `productos` | `Producto` | Artículo con SKU, nombre, precio, categoría e imagen |
| `productos` | `Inventario` | Piezas en exhibición, stock mínimo y alertas de resurtido |
| `productos` | `Almacen` | Piezas en bodega; las pasa a la tienda |
| `productos` | `Proveedor` | Quién surte y en cuántos días se le paga |
| `productos` | `Promocion` | Código de descuento con vigencia y perfil de cliente |
| `personas` | `Persona` | Clase abstracta con los datos comunes de las personas |
| `personas` | `Cliente` | Hereda de Persona; puntos Leones y crédito institucional |
| `personas` | `Personal` | Hereda de Persona; puesto y horario |
| `personas` | `Administrador` | Hereda de Personal; puede cambiar precios y reabastecer según su nivel |
| `compras` | `ItemCarrito` | Una línea del carrito (producto y cantidad) |
| `compras` | `Carrito` | Lista de `ItemCarrito`; calcula subtotal, descuento y total |
| `compras` | `DetallePedido` | Una línea del pedido con el precio del momento |
| `compras` | `Pedido` | Pedido hecho desde la web, con su estado |
| `compras` | `MetodoPago` | Clase abstracta con el método `pagar()` |
| `compras` | `PagoEfectivo`, `PagoTarjeta`, `PagoCredencial` | Cada una paga a su manera (polimorfismo) |
| `compras` | `Venta` | Venta de mostrador: vendedor, cliente, carrito y forma de pago |
| `compras` | `Terminal` | Caja registradora y ticket |
| `compras` | `CierreCaja` | Corte de caja: detecta faltante o sobrante |
| `compras` | `Factura` | Datos fiscales y validación del formato del RFC (**no** se conecta al SAT) |
| `web` | `ServidorWeb` | API en JSON y entrega de los archivos de `web/` |
| (pruebas) | `PruebasClases` | Prueba todas las clases |

El detalle de **atributos, métodos, relaciones y conceptos de POO** está en `documentacion/Documentacion_Clases_AStore.xlsx`.

## Cómo se guardan los datos

- **No hay base de datos.** Mientras el programa corre, los datos viven en `ArrayList` dentro de `Tienda`.
- Cada vez que algo cambia (un pedido, el stock, un precio) se escribe en un archivo **CSV** dentro de `datos/` (por ejemplo `datos/productos.csv`, `datos/pedidos.csv`). Se pueden abrir con Excel.
- Al volver a iniciar, `Tienda` lee esos CSV y recupera todo. Se comprobó con pruebas que pedidos, precios y existencias sobreviven a un reinicio.
- Si la carpeta `datos/` no existe, se crean datos de demostración. **Si la borras, pierdes los pedidos y los cambios.**
- El **carrito** se guarda en el `localStorage` del navegador (solo en ese navegador).
- `datos/` está en `.gitignore`, así que cada computadora tiene sus propios datos.

## Limitaciones conocidas

- El panel `/admin.html` **no tiene contraseña**. El servidor solo escucha en `127.0.0.1`, por lo que únicamente se puede abrir desde la misma computadora.
- Los CSV no están pensados para muchos usuarios a la vez; es un proyecto de clase.
- La lógica del carrito existe dos veces: en Java (`Carrito`, que valida en el servidor) y en JavaScript (`carrito.js`, para la pantalla). Los resultados coinciden (hay pruebas para ambas), pero hay que cambiar las dos si cambian las reglas.
- Las facturas y los pagos con tarjeta son simulaciones: no hay conexión con el SAT ni con bancos.
- Las imágenes son marcadores de posición en SVG.
- Los datos de proveedores y contactos son ficticios.

## Flujo de trabajo en GitHub

La rama `main` siempre debe funcionar. **No se trabaja directamente en `main`.** Cada funcionalidad se hace en su propia rama y se une con un Pull Request (PR).

```bash
# 1. Traer lo último
git checkout main
git pull

# 2. Crear (o cambiar a) tu rama
git checkout -b feature/mi-funcionalidad     # crear
git checkout feature/mi-funcionalidad        # cambiar a una existente

# 3. Hacer cambios, compilar y probar, y guardar el avance
git status
git add .
git commit -m "feat: agregar clase Producto"

# 4. Subir la rama a GitHub
git push -u origin feature/mi-funcionalidad
```

5. En GitHub aparece el botón **Compare & pull request**. Elige `base: main`, escribe qué cambiaste y crea el PR.
6. La otra persona lo revisa (pestaña *Files changed*), comenta si hace falta y lo aprueba.
7. Se une con **Create a merge commit** (no *Squash*, para que las ramas que dependen de otras no den conflictos).
8. Se actualiza el repo local: `git checkout main` y `git pull`.

Si hay conflictos: `git checkout mi-rama`, `git merge main`, se arreglan los archivos marcados, `git add .` y `git commit`.

**Mensajes de commit** (sencillos y descriptivos): `feat:` función nueva, `fix:` corrección, `style:` diseño, `test:` pruebas, `docs:` documentación, `refactor:` reorganizar.

### Ramas

Cada rama parte de la anterior porque unas clases dependen de otras; por eso los Pull Requests se unen **en este orden**:

| # | Rama | Contenido |
|---|---|---|
| 1 | `feature/integracion-base` | Paquetes, `Guardable`, `GestorUsuario`, `.gitignore` y limpieza de archivos viejos |
| 2 | `feature/jose-categorias` | `Categoria` |
| 3 | `feature/jose-productos` | `Producto` |
| 4 | `feature/jose-inventario` | `Inventario`, `Almacen` |
| 5 | `feature/jose-proveedores-promociones` | `Proveedor`, `Promocion` |
| 6 | `feature/paul-usuarios` | `Persona`, `Cliente`, `Personal`, `Administrador` |
| 7 | `feature/paul-carrito` | `ItemCarrito`, `Carrito` |
| 8 | `feature/paul-pedidos` | `DetallePedido`, `Pedido` |
| 9 | `feature/paul-compras` | Métodos de pago y `Venta` |
| 10 | `feature/jose-caja-facturacion` | `Terminal`, `CierreCaja`, `Factura` |
| 11 | `feature/jose-tienda-servidor` | `Tienda`, `ServidorWeb`, `Main`, scripts de ejecución |
| 12 | `feature/jose-catalogo` | Inicio, catálogo y administración de la página |
| 13 | `feature/paul-web-carrito` | Carrito, información y prueba del carrito |
| 14 | `feature/paul-pruebas-java` | `PruebasClases` |
| 15 | `feature/docs-documentacion` | README, Excel y guía para exponer |

## Distribución de responsabilidades

| Integrante | Áreas | Clases |
|---|---|---|
| José Parra | Productos, categorías, inventario, bodega, proveedores, promociones, caja y facturación, tienda y servidor, catálogo web | 14 (`GestorUsuario`, `Guardable`, `Categoria`, `Producto`, `Inventario`, `Almacen`, `Proveedor`, `Promocion`, `Terminal`, `CierreCaja`, `Factura`, `Tienda`, `ServidorWeb`, `Main`) |
| Paul Canto | Usuarios, carrito, pedidos, pagos y ventas, carrito web, pruebas | 14 (`Persona`, `Cliente`, `Personal`, `Administrador`, `ItemCarrito`, `Carrito`, `DetallePedido`, `Pedido`, `MetodoPago`, `PagoEfectivo`, `PagoTarjeta`, `PagoCredencial`, `Venta`, `PruebasClases`) |

Es una distribución propuesta por responsabilidades; el equipo puede ajustarla.

## Créditos

Proyecto de José Parra y Paul Canto, Universidad Anáhuac Cancún, materia Lenguajes Orientados a Objetos. El código base original (clases `Persona`, `Personal`, `clientes`, `productos`, `ventas`, `inventario` y `GestorUsuario`, entre otras) fue reorganizado, corregido y ampliado en este repositorio.
