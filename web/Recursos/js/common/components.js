/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "./util.js";

const spinner = (parent) => {
    const spinner = html(`<span class="ph-spinner ph-spinner--white ph-position--sw ph-component--hidden"></span>`);
    parent.appendChild(spinner);
    
    return {
        show: () => spinner.classList.remove("ph-component--hidden"),
        hidde: () => spinner.classList.add("ph-component--hidden"),
        toggle: () => spinner.classList.toggle("ph-component--hidden")
    };
};

export { spinner };
