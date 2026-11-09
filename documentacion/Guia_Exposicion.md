# Guía para exponer A-Store

Guía sencilla para que **José** y **Paul** entiendan y defiendan el proyecto. Está pensada para nivel principiante-intermedio: cada idea tiene una explicación corta y un ejemplo del código real.

> **Antes de exponer:** el código de este repositorio se reorganizó y amplió con ayuda de Claude (una IA). Revisa la política de tu profesor sobre el uso de herramientas de IA y sé claro con él sobre cómo trabajaron. Lo más importante es que cada uno pueda **explicar su mitad con sus propias palabras y abrir el código mientras lo hace**. Practiquen en voz alta.

---

## 1. ¿Qué hace la página?

A-Store es una tienda universitaria de demostración. Una persona puede:

1. Ver los productos y filtrarlos por categoría o buscarlos por nombre.
2. Meterlos a un carrito, cambiar cantidades y ver el total.
3. Usar un código de descuento.
4. Confirmar un pedido (se paga al recoger; no hay cobro real).

Además hay un panel de administración (`/admin.html`) para cambiar precios y existencias y ver los pedidos.

**Frase para abrir la exposición:** *"A-Store es la tienda de la Anáhuac Cancún hecha como proyecto de clase. La página está en HTML, CSS y JavaScript, y detrás hay un servidor hecho en Java con clases y objetos que guardan los productos, el inventario y los pedidos."*

---

## 2. ¿Cómo está organizada?

Tiene **dos partes que se hablan**:

```
Navegador (HTML, CSS, JavaScript)  <——— pide datos / manda pedidos ———>  Java (ServidorWeb + clases)
        carpeta web/                                                       carpeta src/
                                                                                 |
                                                                        archivos CSV (carpeta datos/)
```

- **web/**: lo que ve el usuario. Tiene 5 páginas (inicio, catálogo, carrito, información y administración).
- **src/astore/**: las clases Java, ordenadas en paquetes: `productos`, `personas`, `compras`, `util` y `web`.
- **datos/**: archivos CSV donde se guarda todo. **No hay base de datos.**

**Cómo se hablan:** cuando abres el catálogo, el JavaScript pide `/api/productos`; el `ServidorWeb` de Java le contesta con la lista de productos en formato JSON y la página dibuja las tarjetas.

---

## 3. ¿Qué hace cada clase?

**Productos (`productos/`)**
- `Categoria`: un grupo (Ropa, Papelería…).
- `Producto`: un artículo con SKU, nombre, precio, categoría e imagen.
- `Inventario`: cuántas piezas hay **en exhibición**. Avisa cuando llega al mínimo.
- `Almacen`: cuántas piezas hay **en bodega**. Puede pasarlas a la tienda.
- `Proveedor`: a quién se le compra y en cuántos días se le paga.
- `Promocion`: código de descuento con fecha límite y perfil (Todos o Estudiantes).

**Personas (`personas/`)**
- `Persona` (abstracta): id, nombre y teléfono.
- `Cliente`: hereda de Persona; tiene puntos Leones y crédito.
- `Personal`: hereda de Persona; tiene puesto y horario.
- `Administrador`: hereda de Personal; puede cambiar precios si su nivel de acceso es 2 o más.

**Compras (`compras/`)**
- `ItemCarrito`: una línea del carrito (producto y cantidad).
- `Carrito`: lista de `ItemCarrito`; calcula subtotal, descuento y total.
- `DetallePedido` y `Pedido`: lo que se guarda cuando se confirma una compra en la web.
- `MetodoPago` (abstracta) con `PagoEfectivo`, `PagoTarjeta` y `PagoCredencial`.
- `Venta`: venta de mostrador (vendedor + cliente + carrito + forma de pago).
- `Terminal`, `CierreCaja` y `Factura`: caja registradora, corte de caja y datos fiscales.

**Generales**
- `Tienda`: **la clase central**. Tiene las listas (`ArrayList`) de todo y coordina los pedidos.
- `ServidorWeb`: conecta Java con la página.
- `GestorUsuario` y `Guardable`: guardan y leen los CSV.
- `Main`: arranca todo.
- `PruebasClases`: comprueba que todo funciona.

---

## 4. ¿Cómo se conectan las clases?

Un resumen en palabras:

- `Tienda` **tiene** listas de `Categoria`, `Producto`, `Inventario`, `Almacen`, `Proveedor`, `Promocion` y `Pedido`.
- Un `Producto` **tiene una** `Categoria`. Un `Inventario` y un `Almacen` **tienen un** `Producto`.
- Un `Carrito` **tiene** `ItemCarrito`, y cada `ItemCarrito` **tiene un** `Producto`.
- Una `Venta` **tiene** un vendedor (`Personal`), un `Cliente`, un `Carrito` y un `MetodoPago`.
- Un `Pedido` **tiene** `DetallePedido`.
- `Cliente`, `Personal` y `Administrador` **son** (heredan de) `Persona`.
- `PagoEfectivo`, `PagoTarjeta` y `PagoCredencial` **son** `MetodoPago`.
- Casi todas **cumplen** la interfaz `Guardable`, por eso `GestorUsuario` las puede guardar.

El Excel (hoja 5) tiene las **87 relaciones** con la línea de código de cada una.

---

## 5. ¿Cómo funciona el catálogo?

1. `catalogo.html` carga `catalogo.js`.
2. `catalogo.js` pide a Java `/api/productos` y `/api/categorias`.
3. `ServidorWeb.listarProductos()` recorre `tienda.getProductos()`, consulta el stock de cada uno en `Inventario` y arma un JSON.
4. El JavaScript dibuja una tarjeta por producto. Los filtros (categoría, búsqueda, "solo disponibles") se hacen en el navegador sobre esa lista.
5. La etiqueta de cada producto sale del stock: **Agotado** (0), **Quedan N** (si `Inventario.alertarFaltante()` es verdadero) o **Disponible**.

---

## 6. ¿Cómo funciona el carrito?

- **En la pantalla:** el carrito se guarda en el `localStorage` del navegador como una lista de `{sku, cantidad}`. `carrito.js` calcula subtotal, descuento y total, y no deja pasar del stock disponible.
- **Al confirmar el pedido:** el navegador manda los productos al servidor (`POST /api/pedidos`). Java vuelve a revisar **todo** (el navegador no es de fiar): existencias, promoción y nombre del cliente. Solo si todo está bien descuenta el inventario y guarda el pedido. Si algo falla no cambia nada.
- **Cálculo:** `subtotal = suma de precio × cantidad`; `descuento = subtotal × porcentaje`; `total = subtotal − descuento`. Ejemplo comprobado: 2 sudaderas ($650) + 1 termo ($250) = $1,550; con `BIENVENIDA10` el descuento es $155 y el total $1,395.

**Dato honesto para decir:** la regla del carrito está en Java (`Carrito`) y también en JavaScript (`carrito.js`); hay pruebas para las dos.

---

## 7. ¿Cómo se administran los productos?

- En `/admin.html` se cambia el **precio** y el **stock** de cada producto y se presiona *Guardar*.
- Eso llama a `Tienda.actualizarProducto()`, que valida (no acepta precios ni stock negativos), actualiza el `Producto` y el `Inventario` y los guarda en los CSV.
- También se ven los pedidos recibidos.
- El panel **no tiene contraseña**: solo funciona en la misma computadora (`localhost`). Es una limitación que conviene mencionar.

**Almacenamiento:** los datos viven en `ArrayList` mientras corre el programa y se guardan en CSV en `datos/` cada vez que algo cambia. Al reiniciar se leen de nuevo. Si borras la carpeta `datos/`, se pierden los pedidos y se vuelven a crear los productos de ejemplo.

---

## 8. Programación orientada a objetos: ejemplos reales

| Concepto | Dónde | Cómo explicarlo |
|---|---|---|
| **Clase y objeto** | `Producto`; en `Main`: `Venta venta = new Venta("VEN-001", cajero, cliente);` | La clase es el molde; `new` crea un objeto concreto. |
| **Encapsulamiento** | `Cliente`: `private int puntosLeones;` y `setPuntosLeones()` | El atributo es privado; para cambiarlo hay que pasar por el setter, que rechaza valores negativos. |
| **Constructores** | `Producto` tiene dos | El corto llama al largo con `this(...)`. Si el SKU está vacío, lanza una excepción. |
| **Herencia** | `Cliente extends Persona` | `Cliente` reutiliza id, nombre y teléfono llamando a `super(...)`. `Administrador` hereda de `Personal` que hereda de `Persona`. |
| **Abstracción** | `Persona` y `MetodoPago` son `abstract`; `Guardable` es una interfaz | No existe una "persona" genérica ni un pago genérico: obligan a las hijas a definir `getTipo()` y `pagar()`. |
| **Polimorfismo** | `for (Persona p : personas) p.describir();` y `metodo.pagar(total)` | Mismo mensaje, respuesta distinta según la clase real: `getTipo()` devuelve "Cliente", "Empleado" o "Administrador". |
| **Interfaz** | `Guardable` | `GestorUsuario.guardar()` acepta cualquier `Guardable` sin saber cuál es. |
| **ArrayList y ciclos** | `Carrito`: `ArrayList<ItemCarrito>` y `for (ItemCarrito item : items)` | Una lista que crece sola, recorrida con `for`. |
| **Excepciones** | Constructores lanzan `IllegalArgumentException`; `ServidorWeb` la atrapa | Se avisa del error en vez de que se caiga el programa. |
| **Relaciones** | `Venta` crea su `Carrito` (composición) y recibe `Personal` y `Cliente` (asociación) | Composición = la clase crea y es dueña del objeto. Asociación = lo recibe ya hecho. |

**Lo que NO usa el proyecto** (por si preguntan): no hay herencia múltiple (Java no la permite), no hay base de datos y no usa frameworks como Spring.

---

## 9. ¿Cómo usamos GitHub?

- La rama **`main`** es la versión buena. No se trabaja directo en ella.
- Cada funcionalidad se hace en su **rama** (`feature/...`), se prueba y se une con un **Pull Request**.
- Las ramas se unen **en orden** porque unas clases dependen de otras (por ejemplo, `Carrito` necesita `Producto` y `Promocion`). La tabla de ramas está en el `README.md`.
- Se usa **merge commit** (no *squash*) para que las ramas encadenadas no den conflictos.
- Los mensajes de commit siguen un formato: `feat:`, `fix:`, `style:`, `test:`, `docs:`, `refactor:`.

**Para hacer en la exposición (si pueden):** abrir la pestaña *Pull requests* del repositorio y mostrar un PR con sus *Files changed*.

---

## 10. ¿Qué desarrolló cada integrante?

Distribución propuesta por áreas. **Ajústenla a lo que realmente haga cada quien y no digan que desarrollaron algo que no pueden explicar.**

| | José Parra | Paul Canto |
|---|---|---|
| **Clases** | `Categoria`, `Producto`, `Inventario`, `Almacen`, `Proveedor`, `Promocion`, `Terminal`, `CierreCaja`, `Factura`, `Tienda`, `ServidorWeb`, `Main`, `GestorUsuario`, `Guardable` | `Persona`, `Cliente`, `Personal`, `Administrador`, `ItemCarrito`, `Carrito`, `DetallePedido`, `Pedido`, `MetodoPago`, `PagoEfectivo`, `PagoTarjeta`, `PagoCredencial`, `Venta`, `PruebasClases` |
| **Página web** | Inicio, catálogo y panel de administración | Carrito, formulario de pedido e información |
| **Qué debe poder explicar** | Cómo se guardan los productos y el inventario, cómo responde el servidor y cómo se arma el catálogo | Cómo funciona la herencia de personas, el carrito, los pagos y los pedidos |
| **Rama(s)** | `feature/jose-*` y `feature/integracion-base` | `feature/paul-*` |

**Consejo:** cada uno debe también entender la mitad del otro a nivel general, porque el profesor puede preguntar a cualquiera.

---

## 11. Preguntas que podría hacer el profesor (con respuestas sencillas)

**1. ¿Por qué `Persona` es abstracta?**
Porque no tiene sentido crear "una persona" a secas; siempre es un cliente, un empleado o un administrador. Al ser abstracta obliga a las hijas a decir su tipo con `getTipo()`.

**2. ¿Qué diferencia hay entre herencia y composición?**
Herencia es "es un": un `Cliente` *es una* `Persona`. Composición es "tiene un": una `Venta` *tiene un* `Carrito`, que crea ella misma.

**3. ¿Dónde se ve el polimorfismo?**
En `Venta.cobrar(MetodoPago metodo)`: llama a `metodo.pagar(total)` y no sabe si es efectivo, tarjeta o credencial; cada clase decide si acepta el pago. También en `Main`, que recorre personas y llama a `describir()`.

**4. ¿Qué es el encapsulamiento y dónde lo usan?**
Que los atributos son `private` y solo se cambian con métodos que validan. Por ejemplo `Cliente.setPuntosLeones()` no acepta números negativos.

**5. ¿Para qué sirve la interfaz `Guardable`?**
Es un contrato: obliga a cada clase a decir su archivo, sus columnas y sus valores. Así `GestorUsuario` guarda cualquier objeto sin conocer su clase.

**6. ¿Por qué no usaron una base de datos?**
La materia no lo pedía y queríamos concentrarnos en Java y POO. Los datos se guardan en archivos CSV y en `ArrayList`. Si el proyecto crece, el siguiente paso sería una base de datos.

**7. ¿Qué pasa con los datos si se apaga el programa?**
Se guardan en los CSV de la carpeta `datos/` cada vez que cambian y se leen al iniciar. Si se borra esa carpeta, se pierden.

**8. ¿Cómo se conecta la página con Java?**
Con una API: el JavaScript pide datos a rutas como `/api/productos` y el `ServidorWeb` de Java responde en JSON. El servidor viene incluido en el JDK.

**9. ¿Por qué el servidor vuelve a revisar el pedido si el carrito ya lo revisó?**
Porque lo que viene del navegador no es de fiar (alguien podría cambiarlo). El servidor valida existencias, promoción y datos, y solo guarda si todo está bien.

**10. ¿Qué pasa si dos personas piden la última pieza?**
Solo uno la recibe. El servidor de Java que usamos atiende las peticiones una por una (no configuramos hilos aparte), así que la revisión de stock y el descuento no se pisan; el segundo pedido recibe "No hay suficiente stock". Lo probamos lanzando 30 pedidos al mismo tiempo por un producto con 3 piezas: se aceptaron exactamente 3, se rechazaron 27 y el stock quedó en 0. *(Aun así es un proyecto de clase, no está diseñado para mucha gente a la vez.)*

**11. ¿Qué es una excepción y dónde la usan?**
Es un aviso de error. Los constructores lanzan `IllegalArgumentException` si les pasan un dato inválido (por ejemplo un precio negativo) y `ServidorWeb` la atrapa y responde con el mensaje en vez de caerse.

**12. ¿Qué es sobrecarga de constructores?**
Tener dos constructores con distintos parámetros. `Producto` tiene uno corto (SKU, nombre, precio) y uno completo (con descripción, categoría e imagen).

**13. ¿Qué hace `ArrayList` y por qué la usan?**
Es una lista que crece sola. `Tienda` guarda ahí los productos y `Carrito` guarda sus líneas. Se recorre con `for`.

**14. ¿Cómo probaron que funciona?**
Con `PruebasClases` (119 comprobaciones de todas las clases, con 0 fallos), con una prueba de los cálculos del carrito en JavaScript, probando todas las rutas de la API y abriendo la página en un navegador automatizado, también simulando una pantalla de celular, y lanzando 30 pedidos simultáneos para comprobar que no se vende más de lo que hay.

**15. ¿Qué no está terminado o qué mejorarían?**
El panel de administración no tiene contraseña, la regla del carrito está escrita en Java y en JavaScript, la factura y el pago con tarjeta son simulaciones y las imágenes son de demostración. Mejoras posibles: base de datos, inicio de sesión y pagos reales.

**16. ¿Cómo trabajaron en GitHub?**
Una rama por funcionalidad, Pull Requests hacia `main`, revisión del compañero y merge en orden. Los commits usan prefijos como `feat:` y `fix:`.

**17. ¿Qué es un Pull Request?**
Una petición para unir una rama con `main`. Permite que el otro integrante revise los cambios antes de unirlos.

**18. ¿Por qué hay clases con el mismo método (por ejemplo `pagar`)?**
Porque son hijas de la misma clase abstracta y cada una lo escribe a su manera. Eso es sobrescritura (`@Override`) y es lo que hace posible el polimorfismo.

---

## 12. Qué decir si algo falla en vivo

- **No abre la página:** revisar que la terminal siga corriendo `ejecutar.sh` / `ejecutar.bat` y abrir **http://localhost:8080**.
- **Puerto ocupado:** `./ejecutar.sh 9090` y abrir `http://localhost:9090`.
- **Salen productos de ejemplo otra vez:** se borró la carpeta `datos/`; es normal.
- **La demostración en consola:** `./ejecutar.sh demo` muestra ventas, pagos, ticket, corte de caja y factura sin necesidad del navegador.
