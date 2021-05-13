/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { escucha } from "../common/util.js";

/**
 * Función para crear un elemento que pueda ocultarse como un pequeño botón
 * circular, que al presionarlo se expanda a su tamaño por defecto
 * 
 * @param {HTMLElement} ocultable El elemento que se desea se pueda ocultar
 * y expandir
 * @returns {any}
 */
const volverExpandible = (ocultable) => {
    
    const eventos = escucha();
    
    const minimizeBtn = ocultable.querySelectorAll(".ph-expandible__minimize-btn");
    const innerDiv = ocultable.querySelector(".ph-expandible__inner");
    
    /**
     * @returns {boolean} Si el componente está minimizado
     */
    const isMinimized = () => ocultable.classList.contains("ph-expandible--minimized");
    
    /**
     * Expande el componente
     */
    const expandir = () => {
        if (!isMinimized()) return;
        
        ocultable.classList.remove("ph-expandible--minimized");
        
        ocultable.style.width = "calc(100% - 40px)";
        ocultable.style.height = (innerDiv.offsetHeight - 33) + "px";

        ocultable.removeEventListener('click', expandir);

        setTimeout(() => {
            ocultable.style = {};
            
            eventos.exec(true);
        }, 400);
    };
    
    /**
     * Minimiza el componente
     */
    const minimizar = () => {
        if (isMinimized()) return;
        
        ocultable.style.width = "calc(100% - 40px)";
        ocultable.style.height = innerDiv.offsetHeight + "px";
        
        ocultable.classList.add("ph-expandible--minimized");
        
        setTimeout(() => {
            ocultable.style.width = "50px";
            ocultable.style.height = "50px";
        }, 1);
        
        setTimeout(() => {
            ocultable.addEventListener('click', expandir);
            
            eventos.exec(false);
        }, 400);
    };
    
    isMinimized() && ocultable.addEventListener('click', expandir);
    
    return {
        expandir,
        minimizar,
        subscribir: eventos.subscribir,
        desubscribir: eventos.desubscribir
    };
    
};

export default volverExpandible;