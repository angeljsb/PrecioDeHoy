/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js";
import { spinner } from "../common/components.js";
import { saveProductDB, editProductDB } from "./api-calls.js";

const FormControl = (form) => {
    let editando = false;
    
    const nombre = form["form-nombre-producto"],
            marca = form["form-marca-producto"],
            unidad = form["form-unit-producto"],
            precio = form["form-precio-producto"],
            descripcion = form["form-descripcion-producto"],
            moneda = form["form-moneda"],
            userId = form["form-id-user"],
            authCode = form["form-auth-user"],
            edit = html(`<input type="hidden" id="form-id-producto" >`);
            
    const minimizeBtn = form.querySelectorAll(".ph-expandible__minimize-btn");
    
    const getPrecioProducto = () => {
        let marcado = precio.value;
        try{
            marcado = JSON.parse(marcado);
        }catch (ex){
            marcado = 0;
        }
        return moneda.value === "Dolar" ? marcado : marcado / window.PrecioDeHoy.PrecioManager.getPrecio();
    };
    
    const getFormJson = () => {
        console.log(userId);
        const json = {
            user_id: userId.value,
            auth_code: authCode.value,
            nombre_producto: nombre.value,
            marca: marca.value,
            unidad: unidad.value,
            descripcion: descripcion.value,
            precio: getPrecioProducto()
        };
        
        if (editando){
            json["producto_id"] = edit.value;
        }
        
        return json;
    };
            
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
        
        window.PrecioDeHoy.controladoresUsuario.expandibleForm.minimizar();
    };
    
    const spinnerObject = spinner(form);
    
    const onEdit = (pro) => {
        window.PrecioDeHoy.controladoresUsuario.productList.editProducto(pro);
        spinnerObject.hidde();
        cancel();
    };
    
    const onSave = (pro) => {
        window.PrecioDeHoy.controladoresUsuario.productList.addProducto(pro);
        spinnerObject.hidde();
        cancel();
    };
    
    form.addEventListener("submit", (e) => {
        e.preventDefault();
        spinnerObject.show();
        
        const action = {
            success: editando ? onEdit : onSave,
            error: console.error
        };
        
        if(editando){
            editProductDB(getFormJson(), action);
        }else{
            saveProductDB(getFormJson(), action); 
        }
    });
    
    minimizeBtn.forEach(btn => btn.addEventListener('click', cancel));
    
    return {
        editar,
        cancel,
        getFormJson
    };
};

export default FormControl;
