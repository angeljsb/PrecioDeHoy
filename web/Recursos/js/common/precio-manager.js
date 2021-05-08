/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import {escucha} from "./util.js";

const PRECIOS = (() => {
    const precios = {};
    
    window.PrecioDeHoy.forEach(precio => {
        const key = precio.proveedor.simbolo;
        const value = precio.precio.numero;
        precios[key] = value;
    });
    
    return precios;
})();

const Precio = (() => {
    let simboloActual = Object.keys(PRECIOS)[0];
    let precio = PRECIOS[simboloActual];
    
    const eventos = escucha();
    
    const setPrecio = (simbolo) => {
        simboloActual = simbolo;
        precio = PRECIOS[simbolo];
        eventos.exec(precio);
    };
    
    return {
        setPrecio,
        getPrecio: () => precio,
        getSimboloActual: () => simboloActual,
        subscribir: eventos.subscribir,
        desubscribir: eventos.desubscribir
    };
})();

export default Precio;
