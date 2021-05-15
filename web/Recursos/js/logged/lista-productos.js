/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js"
import TarjetaProducto from "./tarjeta-producto.js";
import { deleteProductDB } from "./api-calls.js";
import Productos from "./productos-manager.js";

/**
 * Inicializa el mostrado de los productos de un usuario
 * 
 * @param {HTMLElemeent} container El contenedor del listado
 * @returns {any} Objeto con funciones que controlan los productos mostrados
 */
const ListaProductos = (container) => {
    let productos = Productos.getProductos(),
        productosPorPagina = 10,
        pagina = 0;
        
    let tarjetas;
        
    const titulo = html(`<h2 class="ph-container--center-text">Productos</h2>`);
    
    /**
     * Crea los botones de paginación según el estado actual de la lista
     * 
     * @returns {HTMLElement} El elemento con la paginación
     */
    const paginacion = () => {
        const el = html( `
        <div class="ph-pagination">
            <button ${pagina === 0 ? "disabled" : ""} id="prev-page" class="ph-button--icon">
                <svg xmlns="http://www.w3.org/2000/svg" height="24" fill="#ffffff" viewBox="0 0 24 24" width="24"><path d="M0 0h24v24H0z" fill="none"/><path d="M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z"/></svg>
            </button>
            <span class="ph-pagination__label">${pagina}</span>
            <button ${pagina + 1 === getPaginasTotales() ? "disabled" : ""} id="next-page" class="ph-button--icon">
                <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 0 24 24" width="24px" fill="#FFFFFF"><path d="M0 0h24v24H0z" fill="none"/><path d="M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z"/></svg>
            </button>
        </div>` );
        
        el.querySelector("#prev-page").addEventListener("click", () => setPagina(pagina - 1));
        el.querySelector("#next-page").addEventListener("click", () => setPagina(pagina + 1));
        
        return el;
    };
    
    const accionesTarjeta = {
        editar: (pro) => window.PrecioDeHoy.controladoresUsuario.formControl.editar(pro),
        borrar: (pro) => {
            const actions = {
                success: (data) => Productos.removeProducto(data.id),
                error: console.error
            };
            deleteProductDB(pro.id, actions);
        }
    };

    /**
     * Calcula el número de paginasde la lista según los datos actuales
     * 
     * @returns {Number} El número de paginas totales con los productos actuales
     */
    const getPaginasTotales = () => Math.ceil(productos.length / productosPorPagina);
    
    /**
     * Obtiene los productos que deben mostrarse en la pagina actual
     * 
     * @returns {Producto[]} Unarreglo con los productos que deben mostrarse en 
     * la pagina actual
     */
    const getProductosPagina = () => {
        return productos.slice(pagina * productosPorPagina, (pagina * productosPorPagina) + productosPorPagina);
    };
    
    /**
     * Renderiza toda la lista según los datos actuales
     */
    const render = () => {
        container.innerHTML = "";
        
        const productosMostrados = getProductosPagina();
        
        if(productosMostrados.length === 0) return;
        
        const containerProductos = html(`<div class="ph-container--grid"></div>`);
        
        tarjetas = productosMostrados.map((pro) => TarjetaProducto(pro, accionesTarjeta));
        tarjetas.map(tar => tar.elemento()).forEach(pro => containerProductos.appendChild(pro));
        
        container.appendChild(titulo);
        container.appendChild(containerProductos);
        
        if(getPaginasTotales() > 1)
            container.appendChild(paginacion());
        
        setPrecio(window.PrecioDeHoy.PrecioManager.getPrecio());
    };
    
    /**
     * Cambia la pagina de la lista
     * 
     * @param {Number} nuevaPagina El número de pagina al que cambiar
     */
    const setPagina = (nuevaPagina) => {
        if(nuevaPagina < 0 || nuevaPagina >= getPaginasTotales()){
            return;
        }
        pagina = nuevaPagina;
        render();
    };
    
    /**
     * Cambia el precio del dolar al cual se hace la conversión en cada tarjeta
     * 
     * @param {Number} precio El nuevo precio
     */
    const setPrecio = (precio) => {
        if (!tarjetas) return;
        tarjetas.forEach(tarjeta => tarjeta.setPrecio(precio));
    };
    
    const setProductos = (pros) => {
        productos = pros;
        render();
    };
    
    render();
    
    return {
        setPrecio,
        setPagina,
        setProductos
    };
};

export default ListaProductos;