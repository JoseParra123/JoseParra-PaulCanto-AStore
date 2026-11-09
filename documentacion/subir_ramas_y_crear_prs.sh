set -e

REPO="JoseParra123/JoseParra-PaulCanto-AStore"
PIE=''

crear_pr() { # crear_pr <rama> <título> <cuerpo>
  local rama="$1" titulo="$2" cuerpo="$3"
  echo ">>> Subiendo $rama"
  git push -u origin "$rama"
  if gh pr list --repo "$REPO" --head "$rama" --state open --json number --jq '.[0].number' | grep -q .; then
    echo "    (ya existe un PR abierto para $rama, se omite)"
  else
    gh pr create --repo "$REPO" --base main --head "$rama" --title "$titulo" --body "$cuerpo$PIE"
  fi
}

crear_pr feature/integracion-base "refactor: reorganizar el proyecto en paquetes y crear Guardable y GestorUsuario" \
"## Qué hace
- Mueve el código a \`src/astore/\` y elimina los archivos viejos de la raíz.
- Crea la interfaz \`Guardable\` (faltaba y por eso el proyecto no compilaba).
- Renombra \`Gestorusuario.java\` a \`GestorUsuario.java\` para que coincida con la clase.
- Agrega \`cargar()\` para leer los CSV y un \`.gitignore\`.

## Por qué
El proyecto original no compilaba: \`Persona.java\` tenía una llave de más, \`Guardable\` no existía y el archivo de \`GestorUsuario\` tenía otro nombre.

## Cómo probar
\`javac -encoding UTF-8 -d out src/astore/util/*.java\`"

crear_pr feature/jose-categorias "feat: agregar clase Categoria" \
"## Qué hace
Agrega \`Categoria\` (id, nombre, descripción) con validaciones y guardado en CSV.

## Cómo probar
Compila; se prueba en \`PruebasClases\` (rama \`feature/paul-pruebas-java\`)."

crear_pr feature/jose-productos "feat: agregar clase Producto con categoria, descripcion e imagen" \
"## Qué hace
Agrega \`Producto\` con SKU, nombre, precio, descripción, categoría e imagen. Tiene dos constructores (sobrecarga) y \`calcularPrecioConDescuento()\`. El precio no puede ser negativo.

## Depende de
\`Categoria\`."

crear_pr feature/jose-inventario "feat: agregar Inventario y Almacen con control de stock" \
"## Qué hace
- \`Inventario\`: piezas en exhibición, descontar, agregar, merma, conteo físico y alerta de stock mínimo.
- \`Almacen\`: piezas en bodega, recibir embarque, transferir a la tienda, devolver al proveedor y caducidad.

## Depende de
\`Producto\`."

crear_pr feature/jose-proveedores-promociones "feat: agregar Proveedor y Promocion" \
"## Qué hace
- \`Proveedor\`: datos del proveedor, fecha de pago según días de crédito y orden de compra.
- \`Promocion\`: código de descuento con vigencia, perfil de cliente y cálculo del descuento."

crear_pr feature/paul-usuarios "feat: corregir Persona y agregar Cliente, Personal y Administrador" \
"## Qué hace
- \`Persona\` (abstracta): corrige la llave que cerraba la clase antes de tiempo y agrega \`getTipo()\` abstracto.
- \`Cliente\`, \`Personal\` y \`Administrador\` heredan de \`Persona\` (polimorfismo con \`getTipo()\`).
- \`Administrador\` puede cambiar precios y reabastecer según su nivel de acceso.

## Depende de
\`Producto\` e \`Inventario\` (por \`Administrador\`)."

crear_pr feature/paul-carrito "feat: implementar carrito de compras (Carrito e ItemCarrito)" \
"## Qué hace
\`Carrito\` guarda un \`ArrayList\` de \`ItemCarrito\`: agregar (junta productos repetidos), eliminar, cambiar cantidad, subtotal, descuento y total.

## Depende de
\`Producto\` y \`Promocion\`."

crear_pr feature/paul-pedidos "feat: agregar Pedido y DetallePedido" \
"## Qué hace
- \`DetallePedido\`: una línea del pedido con el precio del momento.
- \`Pedido\`: pedido de la web con estados (Pendiente, Pagado, Entregado, Cancelado) y reconstrucción desde el CSV.

## Depende de
\`ItemCarrito\`."

crear_pr feature/paul-compras "feat: agregar metodos de pago y Venta" \
"## Qué hace
- \`MetodoPago\` (abstracta) con \`PagoEfectivo\`, \`PagoTarjeta\` y \`PagoCredencial\` (polimorfismo con \`pagar()\`).
- \`Venta\`: venta de mostrador con vendedor, cliente, carrito y forma de pago.

## Depende de
\`Cliente\`, \`Personal\`, \`Carrito\` y \`Producto\`."

crear_pr feature/jose-caja-facturacion "feat: agregar Terminal, CierreCaja y Factura" \
"## Qué hace
- \`Terminal\`: caja registradora, estados y ticket (antes se llamaba \`Maquinas\`).
- \`CierreCaja\`: corte de caja con faltante o sobrante.
- \`Factura\`: datos fiscales y validación del formato del RFC. **No** se conecta al SAT.

## Depende de
\`Venta\` y \`Personal\`."

crear_pr feature/jose-tienda-servidor "feat: agregar Tienda, servidor web y Main" \
"## Qué hace
- \`Tienda\`: guarda las listas y coordina pedidos (todo o nada), ventas y promociones. Crea datos de demostración la primera vez.
- \`ServidorWeb\`: API en JSON y entrega de la carpeta \`web/\` (servidor incluido en el JDK).
- \`Main\`: inicia la página o la demostración en consola (\`demo\`).
- Scripts \`ejecutar.sh\` y \`ejecutar.bat\`.

## Cómo probar
\`./ejecutar.sh demo\` y \`./ejecutar.sh\` (abrir http://localhost:8080)."

crear_pr feature/jose-catalogo "style: agregar pagina de inicio, catalogo y administracion" \
"## Qué hace
Inicio, catálogo (filtros, búsqueda, disponibilidad) y panel de administración (precio y stock). Estilos generales y 15 imágenes SVG de demostración.

## Cómo probar
Ejecutar el proyecto y abrir \`/\`, \`/catalogo.html\` y \`/admin.html\`. Revisar también con el ancho de un celular."

crear_pr feature/paul-web-carrito "feat: agregar pagina del carrito, informacion y pruebas del carrito" \
"## Qué hace
Página del carrito (cantidades, subtotal, promoción, pedido), página de información y \`pruebas/carrito.test.js\` con los cálculos.

## Cómo probar
\`node pruebas/carrito.test.js\` y probar un pedido completo en la página."

crear_pr feature/paul-pruebas-java "test: agregar pruebas de todas las clases Java" \
"## Qué hace
\`PruebasClases\`: 119 comprobaciones de constructores, validaciones, excepciones, carrito, pagos, caja, pedidos, guardado en CSV y la \`Tienda\`.

## Cómo probar
Ver la sección *Pruebas* del README. Debe decir \`fallidas: 0\`."

crear_pr feature/docs-documentacion "docs: agregar README, Excel de clases y guia para exponer" \
"## Qué hace
- \`README.md\` en español.
- \`documentacion/Documentacion_Clases_AStore.xlsx\`: clases, atributos, métodos, relaciones y POO (generado a partir del código).
- \`documentacion/Guia_Exposicion.md\`.

Al unir este PR, actualicen en la hoja 7 del Excel el número de cada PR."

echo
echo "Listo. Abre https://github.com/$REPO/pulls y une los PR en el orden en que se subieron,"
echo "con 'Create a merge commit' (no 'Squash'). Una persona revisa los PR de la otra."
