/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js";

/**
 * Recibe un objeto con una funcionalidad, el cual puede bloquear remplazandolo
 * con una alerta personalizada
 * 
 * @param {HTMLElement} elemento El elemento a bloquear
 * @returns {Bloqueador.bloqueadorAnonym$0} Un objeto que permite ejecutar el
 * bloqueo y desbloquea así como modificar el contenido de la alerta
 */
const Bloqueador = (elemento) => {
    
    let texto = "Esta funcionalidad está bloqueada";
    let icono = html(`<svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 0 24 24" width="24px" fill="#960200"><path d="M0 0h24v24H0V0z" fill="none"/><path d="M11 7h2v2h-2zm0 4h2v6h-2zm1-9C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8z"/></svg>`);
    let bloqueado = false;
    
    const isBloqueado = () => bloqueado;
    
    const alerta = html(`<div class="ph-aviso">
        <div class="ph-aviso__icono"></div>
        <p class="ph-aviso__texto">${texto}</p>
    </div>`);
    const iconContainer = alerta.querySelector(".ph-aviso__icono");
    const textContainer = alerta.querySelector(".ph-aviso__texto");
    
    const bloquear = () => {
        if (isBloqueado()) return;
        elemento.replaceWith( alerta );
        bloqueado = true;
    };
    
    const desbloquear = () => {
        if (!isBloqueado()) return;
        alerta.replaceWith( elemento );
        bloqueado = false;
    };
    
    const setBloqueado = (bloq) => {
        if (bloq === bloqueado) return;
        bloq ? bloquear() : desbloquear();
    };
    
    const setIcono = (icon) => {
        icono = icon;
        iconContainer.innerHTML = "";
        iconContainer.appendChild(icono);
    };
    
    const setTexto = (text) => {
        texto = text;
        textContainer.innerHTML = text;
    };
    
    const getIcono = () => icono;
    const getTexto = () => texto;
    
    setIcono(icono);
    
    return {
        bloquear,
        desbloquear,
        isBloqueado,
        setBloqueado,
        getIcono,
        setIcono,
        getTexto,
        setTexto
    };
};

export default Bloqueador;
