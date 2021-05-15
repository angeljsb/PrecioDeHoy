/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Bloqueador from "./bloqueador.js";
import Productos from "./productos-manager.js";

const MAXIMO_PRODUCTOS = 30;

const bloqueadorCantidad = (elemento) => {
    const bloqueador = Bloqueador(elemento);
    
    bloqueador.setTexto( "¡Has alcanzado el limite de productos!" );
    
    const evaluarBloqueo = (pro) => {
        bloqueador.setBloqueado(pro.length >= MAXIMO_PRODUCTOS);
    };
    evaluarBloqueo(Productos.getProductos());
    Productos.subscribir(evaluarBloqueo);
    
    return bloqueador;
};

export { bloqueadorCantidad };
