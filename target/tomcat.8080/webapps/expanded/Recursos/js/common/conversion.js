/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Función que incia el control del formulario para realizar conversiones
 * de bolivares a dolares
 * 
 * @param {HTMLElement} container El contenedor del formulario de conversiones
 * @returns {any} Un objeto con funciones para controlar el comportamiento del
 * conversor
 */
const Conversion = ( container ) => {
    const input = container.querySelector(".ph-conversion__input");
    const resultado = container.querySelector(".ph-conversion__result");
    
    const simbolos = container.querySelectorAll(".ph-conversion__symbol");
    
    const button = container.querySelector("button#conversion-button");
    
    let invertido = false;
    let precio = 0;
    
    /**
     * Cambia el estado de un simbolo de moneda de invertido a no invertido.
     * Estar invertido significa que el signo de bolivar se muestre en el
     * text field y el de dolar en el resultado, al contrario de su estado
     * inicial
     * 
     * @param {HTMLElement} simbolo El objeto html que contiene el simbolo de
     * la moneda
     */
    const toggleCambiado = (simbolo) => {
        simbolo.classList.toggle("ph-conversion__symbol--cambiado", invertido);
    };
    
    /**
     * Invierte la conversión entre bolivares a dolares o dolares a bolivares
     */
    const invertir = () => {
        input.value = resultado.value = 0;
        invertido = !invertido;
        simbolos.forEach(toggleCambiado);
    };
    
    /**
     * Realiza la conversión según el precio seleccionado y el valor actual
     * en el input
     */
    const convertir = () => {
        let entrada;
        
        try{
            entrada = JSON.parse(input.value);
        } catch (e){
            entrada = 0;
        }
        
        if (!entrada){
            resultado.value = 0;
            return;
        }
        
        resultado.value = invertido ? `${entrada / precio}` : `${(entrada * precio).toFixed(2)}`;
    };
    
    /**
     * Cambia el precio al que se realiza la conversión
     * 
     * @param {number} nuevoPrecio El nuevo precio seleccionado
     */
    const setPrecio = (nuevoPrecio) => {
        precio = nuevoPrecio;
        convertir();
    };
    
    input.addEventListener("keyup", convertir);
    input.addEventListener("click", convertir);
    
    button.addEventListener("click", invertir);
    
    return {
        invertir,
        convertir,
        setPrecio
    };
};

export default Conversion;