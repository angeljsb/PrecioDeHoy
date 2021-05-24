/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import {escucha} from "./util.js";

const PRECIOS = (() => {
    const precios = {};
    
    window.PrecioDeHoy.proveedores.forEach(precio => {
        const key = precio.proveedor.simbolo;
        const value = precio.precio.numero;
        precios[key] = value;
    });
    
    return precios;
})();

/**
 * Objeto pricipal encargado de controlar los cambios en el precio seleccionado
 * por el usuario
 * 
 */
const Precio = (() => {
    let simboloActual = Object.keys(PRECIOS)[0];
    let precio = PRECIOS[simboloActual];
    
    const eventos = escucha();
    
    /**
     * Cambia el precio actual por el que corresponda al simbolo pasado
     * 
     * @param {"BCV"|"DToday"|"LocalBTC"} simbolo El simbolo del proveedor
     * cuyo precio se quiere usar ahora
     */
    const setPrecio = (simbolo) => {
        simboloActual = simbolo;
        precio = PRECIOS[simbolo];
        eventos.exec(precio);
    };
    
    return {
        setPrecio,
        /**
         * Devuelve el precio del dolar que está usando el usuario
         * 
         * @returns {number} El precio del dolar seleccionado actualmente
         */
        getPrecio: () => precio,
        /**
         * Obtiene el simbolo del proeedor en uso actualmente
         * 
         * @returns {"BCV"|"DToday"|"LocalBTC"} El simbolo del proveedor que 
         * el usuario tenga seleccionado actualmente
         */
        getSimboloActual: () => simboloActual,
        /**
         * Subscribe una función que se activará cada vez que haya un cambio
         * en el precio seleccionado, recibiendo como parametro dicho precio
         * 
         * @param {Function} funcion La función a subscribir
         */
        subscribir: eventos.subscribir,
        desubscribir: eventos.desubscribir
    };
})();

export default Precio;
