/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Precio from "./precio-manager.js";

const Checkboxes = (container) => {
    
    const boxes = container.querySelectorAll('input[type="radio"]');
    
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