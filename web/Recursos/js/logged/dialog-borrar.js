/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Dialog from "../common/dialog.js";
import Productos from "./productos-manager.js";
import { html } from "../common/util.js";
import { deleteProductDB } from "./api-calls.js";

const DialogBorrar = (producto) => {
    const dialog = Dialog();
    
    const title = "Borrar Producto";
    
    const content = html(`<div>
        <p>¿Seguro que desea borrar este producto?</p>
        <ul>
            <li><b>Nombre:</b> ${producto.nombre_producto}</li>
            ${producto.marca ? `<li><b>Marca:</b> ${producto.marca}</li>` : ""}
            ${producto.unidad ? `<li><b>Unidad:</b> ${producto.unidad}</li>` : ""}
            ${producto.descripcion ? `<li><b>Marca:</b> ${producto.descripcion}</li>` : ""}
        </ul>
    </div>`);
    
    const options = [
        {texto: "Cancelar", accion: dialog.close},
        {texto: "Borrar", type: "terciary", accion: () => {
            const actions = {
                success: (data) => {
                    Productos.removeProducto(data.id);
                    dialog.close();
                },
                error: console.error
            };
            deleteProductDB(producto.id, actions);
        }}
    ];
    
    dialog.setTitle(title);
    dialog.setContent(content);
    dialog.setOptions(options);
    dialog.open();
    
    return dialog;
};

export default DialogBorrar;
