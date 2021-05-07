/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Expandible from "./expandible.js";
import showMore from "./show-more.js";

/**
 * 
 * @returns {undefined}
 */
const iniciar = () => {
    const elementos = {
        expandibleForm: document.getElementById("contenedor-formulario"),
        showMoreContainer: document.getElementById("show-more-container"),
        showMoreButton: document.getElementById("show-more-button")
    };
    
    const controladores = {
        expandibleForm: Expandible(elementos.expandibleForm),
        showMore: showMore(elementos.showMoreContainer, elementos.showMoreButton)
    };
};

window.addEventListener("DOMContentLoaded", iniciar);
