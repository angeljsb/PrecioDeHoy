/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Dialog from "./dialog.js";
import { html } from "./util.js";

const DialogError = (error = {}) => {
    const dialog = Dialog();
    
    const title = `Error ${error.status}: ${error.message}`;
    
    const content = html(`<div>
        <p>${error.body}</p>
    </div>`);
    
    const options = [{texto: "Cancelar", accion: dialog.close}];
    
    dialog.setTitle(title);
    dialog.setContent(content);
    dialog.setOptions(options);
    dialog.open();
    
    return dialog;
};

export default DialogError;
