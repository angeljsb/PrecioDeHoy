/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Expandible from "./expandible.js";

/**
 * 
 * @returns {undefined}
 */
const iniciar = () => {
    const elementos = {
        expandibleForm: document.getElementById("contenedor-formulario")
    };
    
    const controladores = {
        expandibleForm: Expandible(elementos.expandibleForm)
    };
};

window.addEventListener("DOMContentLoaded", iniciar);
