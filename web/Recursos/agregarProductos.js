/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let Producto = function (id, nombre, marca = "", precioDolar = 0) {
    this.id = id;
    this.nombre = nombre;
    this.marca = marca;
    this.precioDolar = precioDolar;
    this.precioBolivar = precioDolar * global.precio;
};

Producto.prototype.procesarCambioPrecio = function(){
    this.precioBolivar = this.precioDolar * global.precio;
};

Producto.prototype.constructor = Producto;

const FormularioAgregar = function(){
    this.formulario = document.getElementById("contenedor-formulario");
    this.hidden = true;
}

let Tabla = function (id, productos = [], borrar ={}, editar={}) {
    this.tabla = document.getElementById(id);
    this.id = id;
    
    this.borrar = borrar;
    this.editar = editar;

    this.productos = [];

    for (let producto of productos) {
        this.addProducto(
                new Producto(producto.id, producto.nombre_producto, producto.marca, producto.precio_dolares)
                );
    }

};

Tabla.prototype = {
    constructor: Tabla,
    tableHeaders: ["Nombre del producto", "Marca", "Precio ($)",
        "Precio (Bs)", "Acciones"],
    addProducto: function (producto) {
        if (this.tabla === null) {
            this.crearTabla();
        }
        let tr = this.tabla.insertRow(1);
        tr.innerHTML = `
                <td>${producto.nombre}</td>
                <td>${producto.marca}</td>
                <td>${producto.precioDolar.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2})}</td>
                <td>${producto.precioBolivar.toLocaleString(["es"],{minimumFractionDigits: 2, maximumFractionDigits: 2})}</td>
            `;
        tr.id = "producto-" + producto.id;

        let borrar = document.createElement("button");
        let acciones = document.createElement("td");
        borrar.onclick = ()=>this.borrar(producto.id);
        borrar.innerHTML = "Borrar";
        acciones.appendChild(borrar);
        tr.appendChild(acciones);
        
        this.productos[producto.id] = producto;
    },
    borrarProducto: function (id) {
        let row = document.getElementById("producto-" + id);
        let tablaB = row.parentNode;
        tablaB.removeChild(row);

        this.productos.splice(id, 1);
    },
    crearTabla: function () {

        let container = document.getElementById("contenedor-tabla");
        this.tabla = document.createElement("table");

        this.tabla.id = this.id;
        let thead = this.tabla.insertRow(0);
        
        this.tableHeaders.forEach((header)=>{
            let th = document.createElement("th");
            th.innerHTML = header;
            thead.appendChild(th);
        });

        container.appendChild(this.tabla);
    },
    procesarCambioPrecio: function(){
        this.productos.forEach(producto => {
            producto.procesarCambioPrecio();
            let tr = document.getElementById("producto-" + producto.id);
            let col = tr.getElementsByTagName("td")[3];
            
            col.innerHTML = producto.precioBolivar.toLocaleString(["es"],{minimumFractionDigits: 2, maximumFractionDigits: 2});
        });
    }
};
