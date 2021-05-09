/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js";

/**
 * @typedef {Producto} producto Representa un producto y su info
 * @property {number} id El id del producto en la bd
 * @property {string} nombre_producto El nombre del producto
 * @property {string} marca La marca del producto
 * @property {string} unidad La cantidad del producto
 * @property {string} descripcion Breve descripción del producto
 * @property {number} precio_dolares El precio en dolares
 */

/**
 * 
 * @param {Producto} producto
 * @returns {HTMLElement}
 */
const TarjetaProducto = (producto, on = {}) => {
    const id_producto = producto.id;
    const nombre = producto.nombre_producto || "";
    const marca = producto.marca || "";
    const unidad = producto.unidad || "";
    const descripcion = producto.descripcion || "";
    const precioDolar = producto.precio_dolares || 0;
    
    const localize = (precio) => precio.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2});
    
    const precio_dolares = localize(precioDolar);
    
    const onEditar = on.editar ? () => on.editar(producto) : null;
    const onBorrar = on.borrar ? () => on.borrar(producto) : null;
    
    const elemento = html( `
        <div class="ph-card">
            <div class="ph-card__content" >
                <h3 class="ph-card__title">
                    ${nombre}
                </h3>
                <span>${marca + " "}${unidad ? `(${unidad})` : ""}</span>
                ${ descripcion
                    ? `<p class="ph-card__descripcion">${descripcion}</p>`
                    : "" }
                <div>
                    <table class="ph-container--small-y">
                        <tbody>
                            <tr>
                                <td class="ph-card__moneda">$</td>
                                <td>${precio_dolares}</td>
                            </tr>
                            <tr>
                                <td class="ph-card__moneda">Bs</td>
                                <td class="ph-precio-bolivar"></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="ph-card__acciones ph-container--center-text">
                    <button id="edit-${id_producto}" class="ph-button ph-button--small ph-button--terciary">Editar</button>
                    <button id="delete-${id_producto}" class="ph-button ph-button--small ph-button--primary">Borrar</button>
                </div>
            </div>
        </div>
    ` );
    
    const bolivarTabla = elemento.querySelector(".ph-precio-bolivar");
    
    const botonEditar = elemento.querySelector("button.ph-button--terciary");
    const botonBorrar = elemento.querySelector("button.ph-button--primary");
    
    onEditar && botonEditar.addEventListener("click", onEditar);
    onBorrar && botonBorrar.addEventListener("click", onBorrar);
    
    const setPrecio = (precio) => {
        const bolivar = localize(precioDolar * precio);
        bolivarTabla.innerHTML = bolivar;
    };

    return {
        elemento: () => elemento,
        setPrecio
    };

};

export default TarjetaProducto;
