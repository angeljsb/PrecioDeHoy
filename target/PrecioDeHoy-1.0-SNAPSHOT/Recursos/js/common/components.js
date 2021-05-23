/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "./util.js";

/**
 * Añade un spinner a un componente y retorna funciones para mostrarlo y
 * ocultarlo (inicia oculto)
 * 
 * @param {HTMLElement} parent El elemento al cual añadir el spinner
 * @returns {any} Conjunto de funciones para mostrar y ocultar el spinner
 */
const spinner = (parent) => {
    const spinner = html(`<span class="ph-spinner ph-spinner--white ph-position--sw ph-component--hidden"></span>`);
    parent.appendChild(spinner);
    
    return {
        /**
         * Muestra el spinner
         */
        show: () => spinner.classList.remove("ph-component--hidden"),
        /**
         * Oculta el spinner
         */
        hidde: () => spinner.classList.add("ph-component--hidden"),
        /**
         * Muestra el spinner si está oculto y lo oculta si está visible
         */
        toggle: () => spinner.classList.toggle("ph-component--hidden")
    };
};

export { spinner };
