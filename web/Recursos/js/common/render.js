/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Precio from "./precio-manager.js";
import Conversion from "./conversion.js";
import Checkboxes from "./checkboxes.js";

/**
 * Función encargada de iniciar toda la funcionalidad base de la aplicación.
 * Esta se activa tanto si el usuario está loggeado como si no.
 */
const iniciar = () => {
    const elementos = {
        conversion: document.getElementById("conversion"),
        checboxes: document.getElementById("checkboxes")
    };
    
    const controladores = {
        conversion: Conversion(elementos.conversion),
        checboxes: Checkboxes(elementos.checboxes)
    };
    
    controladores.conversion.setPrecio(Precio.getPrecio());
    
    Precio.subscribir(controladores.conversion.setPrecio);
    Precio.subscribir(controladores.checboxes.updateSelected);
    
    Precio.setPrecio(Precio.getSimboloActual());
};

window.PrecioDeHoy.PrecioManager = Precio;
window.addEventListener("DOMContentLoaded", iniciar);
