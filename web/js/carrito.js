// Lógica de la página del carrito: cantidades, subtotales, total, promoción y envío del pedido.

// ---------- Cálculos (no tocan la página, por eso se pueden probar solos) ----------

function redondear(numero) {
  return Math.round(numero * 100) / 100;
}

/**
 * Calcula el resumen del carrito igual que la clase Java Carrito:
 * subtotal = suma de precio x cantidad, descuento = subtotal x porcentaje, total = subtotal - descuento.
 */
function calcularResumen(items, catalogo, porcentaje) {
  var lineas = [];
  var suma = 0;
  items.forEach(function (item) {
    var producto = catalogo.find(function (p) { return p.sku === item.sku; });
    if (!producto) return;
    var subtotalLinea = producto.precio * item.cantidad;
    suma += subtotalLinea;
    lineas.push({ producto: producto, cantidad: item.cantidad, subtotal: redondear(subtotalLinea) });
  });
  var subtotal = redondear(suma);
  var descuento = Math.round(subtotal * (porcentaje || 0)) / 100;
  return { lineas: lineas, subtotal: subtotal, descuento: descuento, total: redondear(subtotal - descuento) };
}

if (typeof module !== 'undefined') {
  module.exports = { calcularResumen: calcularResumen, redondear: redondear };
}

// ---------- Página del carrito (solo se ejecuta en el navegador) ----------

if (typeof document !== 'undefined') {
  var catalogo = [];
  var porcentaje = 0;
  var promoAplicada = '';

  var iniciarCarrito = async function () {
    try {
      catalogo = await pedirJSON('api/productos');
    } catch (e) {
      document.getElementById('lista-carrito').innerHTML = mensajeServidor();
      document.getElementById('resumen').hidden = true;
      return;
    }
    ajustarCarritoAlCatalogo();
    pintarCarrito();

    document.getElementById('lista-carrito').addEventListener('click', alHacerClic);
    document.getElementById('lista-carrito').addEventListener('change', alCambiarCantidad);
    document.getElementById('aplicar-promo').addEventListener('click', aplicarPromocion);
    document.getElementById('estudiante').addEventListener('change', function () {
      if (document.getElementById('codigo-promo').value.trim()) aplicarPromocion();
    });
    document.getElementById('formulario-pedido').addEventListener('submit', enviarPedido);
  };

  /** Quita del carrito lo que ya no existe o está agotado, y limita las cantidades al stock. */
  var ajustarCarritoAlCatalogo = function () {
    var carrito = leerCarrito();
    var ajustado = [];
    carrito.forEach(function (item) {
      var p = catalogo.find(function (x) { return x.sku === item.sku; });
      if (p && p.stock > 0 && item.cantidad > 0) {
        ajustado.push({ sku: item.sku, cantidad: Math.min(item.cantidad, p.stock) });
      }
    });
    if (JSON.stringify(ajustado) !== JSON.stringify(carrito)) {
      guardarCarrito(ajustado);
      mostrarAviso('Se actualizó tu carrito según las existencias.');
    }
  };

  var pintarCarrito = function () {
    var items = leerCarrito();
    var resumen = calcularResumen(items, catalogo, porcentaje);
    var lista = document.getElementById('lista-carrito');

    if (resumen.lineas.length === 0) {
      lista.innerHTML = '<p class="vacio">Tu carrito está vacío. <a href="catalogo.html">Ir al catálogo</a></p>';
    } else {
      lista.innerHTML = resumen.lineas.map(function (l) {
        var p = l.producto;
        return '<article class="linea">' +
          '<img src="' + escaparHTML(p.imagen) + '" alt="' + escaparHTML(p.nombre) + ' (imagen de demostración)">' +
          '<div><h3>' + escaparHTML(p.nombre) + '</h3><p>' + dinero(p.precio) + ' c/u · Disponibles: ' + p.stock + '</p></div>' +
          '<div class="cantidad">' +
          '<button type="button" data-menos="' + escaparHTML(p.sku) + '" aria-label="Quitar una pieza">−</button>' +
          '<input type="number" min="0" max="' + p.stock + '" value="' + l.cantidad + '" data-cantidad="' + escaparHTML(p.sku) + '" aria-label="Cantidad de ' + escaparHTML(p.nombre) + '">' +
          '<button type="button" data-mas="' + escaparHTML(p.sku) + '" aria-label="Agregar una pieza">+</button>' +
          '</div>' +
          '<div class="linea-subtotal">' + dinero(l.subtotal) + '</div>' +
          '<button type="button" class="quitar" data-quitar="' + escaparHTML(p.sku) + '">Eliminar</button>' +
          '</article>';
      }).join('');
    }

    document.getElementById('res-subtotal').textContent = dinero(resumen.subtotal);
    document.getElementById('res-descuento').textContent = '-' + dinero(resumen.descuento);
    document.getElementById('fila-descuento').hidden = (resumen.descuento === 0);
    document.getElementById('res-total').textContent = dinero(resumen.total);
    document.getElementById('boton-pedido').disabled = (resumen.lineas.length === 0);
  };

  /** Cambia la cantidad de un producto del carrito (0 lo elimina). */
  var cambiarCantidad = function (sku, nueva) {
    var p = catalogo.find(function (x) { return x.sku === sku; });
    var carrito = leerCarrito();
    var linea = carrito.find(function (x) { return x.sku === sku; });
    if (!p || !linea) return;
    if (isNaN(nueva) || nueva < 1) {
      carrito = carrito.filter(function (x) { return x.sku !== sku; });
    } else if (nueva > p.stock) {
      mostrarAviso('Solo hay ' + p.stock + ' pieza(s) disponibles.');
      linea.cantidad = p.stock;
    } else {
      linea.cantidad = nueva;
    }
    guardarCarrito(carrito);
    pintarCarrito();
  };

  var alHacerClic = function (evento) {
    var objetivo = evento.target;
    var sku;
    if ((sku = objetivo.getAttribute('data-mas'))) {
      var linea = leerCarrito().find(function (x) { return x.sku === sku; });
      cambiarCantidad(sku, linea.cantidad + 1);
    } else if ((sku = objetivo.getAttribute('data-menos'))) {
      var linea2 = leerCarrito().find(function (x) { return x.sku === sku; });
      cambiarCantidad(sku, Math.max(1, linea2.cantidad - 1));
    } else if ((sku = objetivo.getAttribute('data-quitar'))) {
      cambiarCantidad(sku, 0);
    }
  };

  var alCambiarCantidad = function (evento) {
    var sku = evento.target.getAttribute('data-cantidad');
    if (sku) cambiarCantidad(sku, parseInt(evento.target.value, 10));
  };

  var perfilElegido = function () {
    return document.getElementById('estudiante').checked ? 'Estudiantes' : 'Todos';
  };

  var aplicarPromocion = async function () {
    var codigo = document.getElementById('codigo-promo').value.trim();
    var mensaje = document.getElementById('mensaje-promo');
    if (!codigo) {
      porcentaje = 0;
      promoAplicada = '';
      mensaje.textContent = '';
      pintarCarrito();
      return;
    }
    try {
      var r = await pedirJSON('api/promocion?codigo=' + encodeURIComponent(codigo) + '&perfil=' + perfilElegido());
      if (r.valida) {
        porcentaje = r.porcentaje;
        promoAplicada = codigo;
        mensaje.textContent = 'Promoción "' + r.nombre + '" aplicada: ' + r.porcentaje + '% de descuento.';
        mensaje.style.color = 'var(--verde)';
      } else {
        porcentaje = 0;
        promoAplicada = '';
        mensaje.textContent = r.mensaje;
        mensaje.style.color = 'var(--rojo-oscuro)';
      }
    } catch (e) {
      mensaje.textContent = 'No se pudo consultar la promoción.';
    }
    pintarCarrito();
  };

  var enviarPedido = async function (evento) {
    evento.preventDefault();
    var items = leerCarrito();
    if (items.length === 0) return;

    var boton = document.getElementById('boton-pedido');
    var estado = document.getElementById('estado-pedido');
    boton.disabled = true;
    estado.innerHTML = '';

    var cuerpo = new URLSearchParams({
      nombre: document.getElementById('nombre').value,
      correo: document.getElementById('correo').value,
      metodo: document.getElementById('metodo').value,
      codigo: promoAplicada,
      perfil: perfilElegido(),
      items: items.map(function (i) { return i.sku + ':' + i.cantidad; }).join(',')
    });

    try {
      var r = await pedirJSON('api/pedidos', { method: 'POST', body: cuerpo });
      if (r.ok) {
        guardarCarrito([]);
        document.getElementById('contenido-carrito').hidden = true;
        document.getElementById('confirmacion').hidden = false;
        document.getElementById('conf-folio').textContent = r.folio;
        document.getElementById('conf-total').textContent = dinero(r.total);
      } else {
        estado.innerHTML = '<div class="mensaje error">' + escaparHTML(r.error) + '</div>';
        boton.disabled = false;
      }
    } catch (e) {
      estado.innerHTML = mensajeServidor();
      boton.disabled = false;
    }
  };

  document.addEventListener('DOMContentLoaded', iniciarCarrito);
}
