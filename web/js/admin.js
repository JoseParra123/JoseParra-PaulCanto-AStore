// Panel de administración de demostración: editar precio y stock, y ver pedidos.

async function cargarAdmin() {
  try {
    var productos = await pedirJSON('api/productos');
    var pedidos = await pedirJSON('api/admin/pedidos');
    pintarProductos(productos);
    pintarPedidos(pedidos);
  } catch (e) {
    document.getElementById('estado').innerHTML = mensajeServidor();
  }
}

function pintarProductos(productos) {
  document.getElementById('tabla-productos').innerHTML = productos.map(function (p) {
    return '<tr>' +
      '<td>' + escaparHTML(p.sku) + '</td>' +
      '<td>' + escaparHTML(p.nombre) + '</td>' +
      '<td>' + escaparHTML(p.categoria) + '</td>' +
      '<td><input type="number" min="0" step="0.01" id="precio-' + escaparHTML(p.sku) + '" value="' + p.precio + '"></td>' +
      '<td><input type="number" min="0" step="1" id="stock-' + escaparHTML(p.sku) + '" value="' + p.stock + '"></td>' +
      '<td><button class="boton" data-guardar="' + escaparHTML(p.sku) + '">Guardar</button></td>' +
      '</tr>';
  }).join('');
}

function pintarPedidos(pedidos) {
  var cuerpo = document.getElementById('tabla-pedidos');
  if (pedidos.length === 0) {
    cuerpo.innerHTML = '<tr><td colspan="7" class="vacio">Todavía no hay pedidos.</td></tr>';
    return;
  }
  cuerpo.innerHTML = pedidos.map(function (p) {
    return '<tr><td>' + escaparHTML(p.folio) + '</td><td>' + escaparHTML(p.fecha) + '</td><td>' + escaparHTML(p.cliente) +
      '</td><td>' + escaparHTML(p.metodo) + '</td><td>' + escaparHTML(p.estado) + '</td><td>' + dinero(p.total) +
      '</td><td style="white-space:normal">' + escaparHTML(p.detalle) + '</td></tr>';
  }).join('');
}

async function guardarProducto(sku) {
  var cuerpo = new URLSearchParams({
    sku: sku,
    precio: document.getElementById('precio-' + sku).value,
    stock: document.getElementById('stock-' + sku).value
  });
  var estado = document.getElementById('estado');
  try {
    var respuesta = await pedirJSON('api/admin/actualizar', { method: 'POST', body: cuerpo });
    if (respuesta.ok) {
      estado.innerHTML = '<div class="mensaje exito">Se guardó ' + escaparHTML(sku) + '.</div>';
    } else {
      estado.innerHTML = '<div class="mensaje error">' + escaparHTML(respuesta.error) + '</div>';
    }
  } catch (e) {
    estado.innerHTML = mensajeServidor();
  }
}

document.addEventListener('click', function (evento) {
  var boton = evento.target.closest('[data-guardar]');
  if (boton) {
    guardarProducto(boton.getAttribute('data-guardar'));
  }
});

document.addEventListener('DOMContentLoaded', cargarAdmin);
