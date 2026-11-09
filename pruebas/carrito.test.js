
const assert = require('assert');
const { calcularResumen } = require('../web/js/carrito.js');

const catalogo = [
  { sku: 'A', precio: 650 },
  { sku: 'B', precio: 250 },
  { sku: 'C', precio: 0.1 },
];

// Subtotal sin descuento
let r = calcularResumen([{ sku: 'A', cantidad: 2 }, { sku: 'B', cantidad: 1 }], catalogo, 0);
assert.strictEqual(r.subtotal, 1550);
assert.strictEqual(r.descuento, 0);
assert.strictEqual(r.total, 1550);

// Descuento del 10%
r = calcularResumen([{ sku: 'A', cantidad: 2 }, { sku: 'B', cantidad: 1 }], catalogo, 10);
assert.strictEqual(r.descuento, 155);
assert.strictEqual(r.total, 1395);

// Descuento del 15% sobre 1,550 = 232.5
r = calcularResumen([{ sku: 'A', cantidad: 2 }, { sku: 'B', cantidad: 1 }], catalogo, 15);
assert.strictEqual(r.descuento, 232.5);
assert.strictEqual(r.total, 1317.5);

// Decimales: 3 x 0.10 debe dar 0.30 (no 0.30000000000000004)
r = calcularResumen([{ sku: 'C', cantidad: 3 }], catalogo, 0);
assert.strictEqual(r.subtotal, 0.3);

// Producto que ya no existe en el catálogo: se ignora
r = calcularResumen([{ sku: 'Z', cantidad: 5 }], catalogo, 0);
assert.strictEqual(r.lineas.length, 0);
assert.strictEqual(r.total, 0);

// Carrito vacío
r = calcularResumen([], catalogo, 10);
assert.strictEqual(r.total, 0);

console.log('Pruebas del carrito: 6 grupos de comprobaciones OK');
