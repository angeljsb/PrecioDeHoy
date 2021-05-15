/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { escucha } from "../common/util.js";

const Productos = (() => {
    let productos = window.PrecioDeHoy.productos;
    
    const eventos = escucha();
    
    /**
     * Añade un producto
     * 
     * @param {Producto} producto El nuevo producto
     */
    const addProducto = (producto) => {
        const rev = productos.reverse();
        rev.push(producto);
        window.PrecioDeHoy.productos = productos = rev.reverse();
        eventos.exec(productos);
    };
    
    /**
     * Edita un producto
     * 
     * @param {Producto} producto El producto a editado
     */
    const editProducto = (producto) => {
        const id = producto.id;
        const match = (otro) => otro.id === id;
        const editado = productos.findIndex(match);
        
        if(editado === -1) return;
        
        productos[editado] = producto;
        window.PrecioDeHoy.productos = productos;
        eventos.exec(productos);
    };
    
    /**
     * Quita un producto de la lista
     * 
     * @param {Number} idProducto El id del producto a quitar
     */
    const removeProducto = (idProducto) => {
        const match = (otro) => otro.id === idProducto;
        const editado = productos.findIndex(match);
        
        if(editado === -1) return;
        
        productos.splice(editado, 1);
        window.PrecioDeHoy.productos = productos;
        eventos.exec(productos);
    };
    
    return {
        addProducto,
        editProducto,
        removeProducto,
        getProductos: () => productos,
        subscribir: eventos.subscribir,
        desubscribir: eventos.desubscribir
    };
})();

export default Productos;
