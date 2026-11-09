// Lógica de la página del catálogo: filtros por categoría, búsqueda y disponibilidad.

var productos = [];
var categorias = [];
var categoriaActual = 'todas';

async function iniciarCatalogo() {
  try {
    categorias = await pedirJSON('api/categorias');
    productos = await pedirJSON('api/productos');
  } catch (e) {
    document.getElementById('productos').innerHTML = mensajeServidor();
    return;
  }

  // Si la dirección trae ?cat=ropa, se abre esa categoría
  var pedida = new URLSearchParams(window.location.search).get('cat');
  if (pedida && categorias.some(function (c) { return c.id === pedida; })) {
    categoriaActual = pedida;
  }

  pintarFiltros();
  pintarProductos();

  document.getElementById('buscar').addEventListener('input', pintarProductos);
  document.getElementById('solo-disponibles').addEventListener('change', pintarProductos);
}

function pintarFiltros() {
  var botones = [{ id: 'todas', nombre: 'Todas' }].concat(categorias);
  var contenedor = document.getElementById('filtros');
  contenedor.innerHTML = botones.map(function (c) {
    var clase = (c.id === categoriaActual) ? 'chip activo' : 'chip';
    return '<button class="' + clase + '" data-cat="' + escaparHTML(c.id) + '">' + escaparHTML(c.nombre) + '</button>';
  }).join('');

  contenedor.querySelectorAll('button').forEach(function (boton) {
    boton.addEventListener('click', function () {
      categoriaActual = boton.getAttribute('data-cat');
      pintarFiltros();
      pintarProductos();
    });
  });
}

function pintarProductos() {
  var texto = document.getElementById('buscar').value.trim().toLowerCase();
  var soloDisponibles = document.getElementById('solo-disponibles').checked;

  var lista = productos.filter(function (p) {
    var coincideCategoria = (categoriaActual === 'todas') || (p.categoriaId === categoriaActual);
    var coincideTexto = p.nombre.toLowerCase().indexOf(texto) !== -1 || p.descripcion.toLowerCase().indexOf(texto) !== -1;
    var coincideStock = !soloDisponibles || p.disponible;
    return coincideCategoria && coincideTexto && coincideStock;
  });

  document.getElementById('conteo').textContent = lista.length + ' producto(s)';
  document.getElementById('productos').innerHTML = lista.length
    ? lista.map(crearTarjeta).join('')
    : '<p class="vacio">No se encontraron productos con esos filtros.</p>';
}

document.addEventListener('DOMContentLoaded', iniciarCatalogo);
