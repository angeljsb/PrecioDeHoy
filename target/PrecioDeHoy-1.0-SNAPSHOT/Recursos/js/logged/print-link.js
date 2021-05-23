/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Precio from "../common/precio-manager.js";

const PrintLink = (link) => {
    const baseRef = link.href;
    
    const setPrecio = (price) => {
        link.href = baseRef + "?precio=" + price 
                + "&simbolo=" + Precio.getSimboloActual();
    };
    
    Precio.subscribir(setPrecio);
};

export default PrintLink;
