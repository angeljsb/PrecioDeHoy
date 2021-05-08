/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Precio from "./precio-manager.js";
import Conversion from "./conversion.js";

const iniciar = () => {
    const elementos = {
        conversion: document.getElementById("conversion"),
    };
    
    const controladores = {
        conversion: Conversion(elementos.conversion)
    };
    
    controladores.conversion.setPrecio(Precio.getPrecio());
    
};

window.addEventListener("DOMContentLoaded", iniciar);
