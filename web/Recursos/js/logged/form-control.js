/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js";
import { spinner } from "../common/components.js";
import { saveProductDB, editProductDB } from "./api-calls.js";

/**
 * Inicia el control del formulario de agregar y editar productos
 * 
 * @param {HTMLFormElement} form El formulario
 * @returns {any} Un objeto con funciones para controlar el formulario
 */
const FormControl = (form) => {
    let editando = false;
    
    const nombre = form["form-nombre-producto"],
            marca = form["form-marca-producto"],
            unidad = form["form-unit-producto"],
            precio = form["form-precio-producto"],
            descripcion = form["form-descripcion-producto"],
            moneda = form["form-moneda"],
            edit = html(`<input type="hidden" id="form-id-producto" >`);
            
    const minimizeBtn = form.querySelectorAll(".ph-expandible__minimize-btn");
    
    /**
     * Obtiene el precio en dolares añadido actualmente al formulario según el 
     * input de precio y el de moneda escogidos actualmente
     * 
     * @returns {Number} El precio en dolares marcado actualmente
     */
    const getPrecioProducto = () => {
        let marcado = precio.value;
        try{
            marcado = JSON.parse(marcado);
        }catch (ex){
            marcado = 0;
        }
        return moneda.value === "Dolar" ? marcado : marcado / window.PrecioDeHoy.PrecioManager.getPrecio();
    };
    
    /**
     * Devuelve los datos escritos en el formulario en el formato de un producto
     * 
     * @returns {Producto} El producto creado a partir de los datos en el formulario
     */
    const getFormJson = () => {
        const json = {
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

    /**
     * Inicia la edición de un producto. Esto añade los campos del producto
     * en todos los input del formulario e inicia el modo de edición
     * 
     * @param {Producto} producto El producto a editar
     */
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
        window.PrecioDeHoy.controladoresUsuario.showMore.cambiar(Boolean(descripcion.value));
    };
    
    /**
     * Reinicia el formulario y cancela el modo de edición
     */
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
