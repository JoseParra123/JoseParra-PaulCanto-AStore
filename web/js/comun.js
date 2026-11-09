// Funciones compartidas por todas las páginas de A-Store.

var CLAVE_CARRITO = 'astore_carrito'; // el carrito se guarda en el navegador (localStorage)

/** Da formato de dinero: 1550 -> $1,550.00 */
function dinero(numero) {
  return '$' + Number(numero).toLocaleString('es-MX', { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

/** Evita que un texto se interprete como HTML. */
function escaparHTML(texto) {
  return String(texto == null ? '' : texto)
    .replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;').replace(/'/g, '&#39;');
}

/** Pide datos al servidor Java y los devuelve como objeto. */
async function pedirJSON(url, opciones) {
  var respuesta = await fetch(url, opciones);
  return respuesta.json();
}

/** Mensaje que se muestra si el servidor Java no está encendido. */
function mensajeServidor() {
  return '<div class="mensaje error">No se pudo conectar con el servidor de A-Store. ' +
    'Inicia el programa Java (ver README) y abre <strong>http://localhost:8080</strong>.</div>';
}

// ----- Carrito en localStorage: lista de { sku, cantidad } -----

function leerCarrito() {
  try {
    var datos = JSON.parse(localStorage.getItem(CLAVE_CARRITO));
    return Array.isArray(datos) ? datos : [];
  } catch (e) {
    return [];
  }
}

function guardarCarrito(carrito) {
  try {
    localStorage.setItem(CLAVE_CARRITO, JSON.stringify(carrito));
  } catch (e) {
    mostrarAviso('No se pudo guardar el carrito en este navegador.');
  }
  actualizarContador();
}

/** Agrega piezas de un producto. No deja pasar del stock disponible. */
function agregarAlCarrito(sku, cantidad, stock) {
  var carrito = leerCarrito();
  var linea = carrito.find(function (item) { return item.sku === sku; });
  var actual = linea ? linea.cantidad : 0;
  if (actual + cantidad > stock) {
    mostrarAviso('Solo hay ' + stock + ' pieza(s) disponibles.');
    return;
  }
  if (linea) {
    linea.cantidad += cantidad;
  } else {
    carrito.push({ sku: sku, cantidad: cantidad });
  }
  guardarCarrito(carrito);
  mostrarAviso('Producto agregado al carrito.');
}

function actualizarContador() {
  var el = document.getElementById('contador-carrito');
  if (!el) return;
  var total = leerCarrito().reduce(function (suma, item) { return suma + item.cantidad; }, 0);
  el.textContent = total;
}

function mostrarAviso(texto) {
  var aviso = document.getElementById('aviso-flotante');
  if (!aviso) {
    aviso = document.createElement('div');
    aviso.id = 'aviso-flotante';
    aviso.className = 'aviso-flotante';
    aviso.setAttribute('role', 'status');
    document.body.appendChild(aviso);
  }
  aviso.textContent = texto;
  aviso.style.display = 'block';
  clearTimeout(aviso.temporizador);
  aviso.temporizador = setTimeout(function () { aviso.style.display = 'none'; }, 2200);
}

/** HTML de una tarjeta de producto (se usa en el inicio y en el catálogo). */
function crearTarjeta(p) {
  var estado;
  if (!p.disponible) {
    estado = '<span class="etiqueta agotado">Agotado</span>';
  } else if (p.pocasPiezas) {
    estado = '<span class="etiqueta pocas">Quedan ' + p.stock + '</span>';
  } else {
    estado = '<span class="etiqueta disponible">Disponible</span>';
  }
  return '<article class="tarjeta">' +
    '<div class="tarjeta-img"><img src="' + escaparHTML(p.imagen) + '" alt="' + escaparHTML(p.nombre) + ' (imagen de demostración)" loading="lazy"></div>' +
    '<div class="tarjeta-cuerpo">' +
    '<p class="categoria-txt">' + escaparHTML(p.categoria) + '</p>' +
    '<h3>' + escaparHTML(p.nombre) + '</h3>' +
    '<p class="desc">' + escaparHTML(p.descripcion) + '</p>' +
    '<div class="fila"><span class="precio">' + dinero(p.precio) + '</span>' + estado + '</div>' +
    '<button class="boton boton-bloque" data-agregar="' + escaparHTML(p.sku) + '" data-stock="' + p.stock + '"' +
    (p.disponible ? '' : ' disabled') + '>Agregar al carrito</button>' +
    '</div></article>';
}

// Un solo "oyente" atiende todos los botones "Agregar al carrito" de la página
document.addEventListener('click', function (evento) {
  var boton = evento.target.closest('[data-agregar]');
  if (boton && !boton.disabled) {
    agregarAlCarrito(boton.getAttribute('data-agregar'), 1, parseInt(boton.getAttribute('data-stock'), 10));
  }
});

document.addEventListener('DOMContentLoaded', actualizarContador);
