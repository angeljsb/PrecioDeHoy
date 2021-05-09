/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const Conversion = ( container ) => {
    const input = container.querySelector(".ph-conversion__input");
    const resultado = container.querySelector(".ph-conversion__result");
    
    const simbolos = container.querySelectorAll(".ph-conversion__symbol");
    
    const button = container.querySelector("button#conversion-button");
    
    let invertido = false;
    let precio = 0;
    
    const toggleCambiado = (simbolo) => {
        simbolo.classList.toggle("ph-conversion__symbol--cambiado", invertido);
    };
    
    const invertir = () => {
        input.value = resultado.value = 0;
        invertido = !invertido;
        simbolos.forEach(toggleCambiado);
    };
    
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