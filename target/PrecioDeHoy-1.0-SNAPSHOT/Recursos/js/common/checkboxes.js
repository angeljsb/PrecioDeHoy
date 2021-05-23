/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Precio from "./precio-manager.js";

/**
 * Inicia el control de los input de tipo radio para seleccionar el precio
 * que se desea utilizar para las conversiones
 * 
 * @param {HTMLElement} container El contenedor de los input
 * @returns {any} Un objeto con funciones para el control de las radio boxes
 */
const Checkboxes = (container) => {
    
    const boxes = container.querySelectorAll('input[type="radio"]');
    
    /**
     * Actualiza el radiobox seleccionado según el precio seleccionado
     * actualmente
     */
    const updateSelected = () => {
        const simbolo = Precio.getSimboloActual();
        container.querySelector("#check-" + simbolo).checked = true;
    };
    
    const onClick = (e) => {
        const target = e.target;
        const simbolo = target.dataset.symbol;
        
        Precio.setPrecio(simbolo);
    };
    
    updateSelected();
    
    boxes.forEach(box => box.addEventListener("click", onClick));
    
    return {
        updateSelected
    };
};

export default Checkboxes;