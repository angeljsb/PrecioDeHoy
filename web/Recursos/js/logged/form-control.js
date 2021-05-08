/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js";

const FormControl = (form) => {
    let editando = false;
    
    const nombre = form["form-nombre-producto"],
            marca = form["form-marca-producto"],
            unidad = form["form-unit-producto"],
            precio = form["form-precio-producto"],
            descripcion = form["form-descripcion-producto"],
            moneda = form["form-moneda"],
            edit = html(`<input type="hidden" id="form-id-producto" >`);
            
    const editar = (producto) => {
        if (!producto || !producto.id) return;
        
        if (!editando){
            form.appendChild(edit);
        }
        
        editando = true;
        
        nombre.value = producto.nombre_producto || "";
        marca.value = producto.marca || "";
        unidad.value = producto.unidad || "";
        precio.value = producto.precio_dolares || 0;
        descripcion.value = producto.descripcion || "";
        moneda.value = "Dolar";
        edit.value = producto.id;
        
        window.PrecioDeHoy.controladoresUsuario.expandibleForm.expandir();
        window.PrecioDeHoy.controladoresUsuario.showMore.cambiar(!descripcion.value.isEmpty());
    };
    
    const cancel = () => {
        if(editando){
            edit.value = null;
            form.removeChild(edit);
        }
        editando = false;
        form.reset();
    };
    
    return {
        editar,
        cancel
    };
};

export default FormControl;
