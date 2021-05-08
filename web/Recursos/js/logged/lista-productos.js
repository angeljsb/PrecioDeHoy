/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import { html } from "../common/util.js"
import TarjetaProducto from "./tarjeta-producto.js";

const ListaProductos = (container) => {
    let productos = window.PrecioDeHoy.productos,
        productosPorPagina = 10,
        pagina = 0;
        
    let tarjetas;
        
    const titulo = html(`<h2 class="ph-container--center-text">Productos</h2>`);
    
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
        editar: (pro) => window.PrecioDeHoy.controladoresUsuario.formControl.editar(pro)
    };

    const getPaginasTotales = () => Math.ceil(productos.length / productosPorPagina);
    
    const getProductosPagina = () => {
        return productos.slice(pagina * productosPorPagina, (pagina * productosPorPagina) + productosPorPagina);
    };
    
    const render = () => {
        container.innerHTML = "";
        
        const productosMostrados = getProductosPagina();
        
        if(productosMostrados.length === 0) return;
        
        const containerProductos = html(`<div class="ph-container--grid"></div>`);
        
        tarjetas = productosMostrados.map((pro) => TarjetaProducto(pro, accionesTarjeta));
        tarjetas.map(tar => tar.elemento()).forEach(pro => containerProductos.appendChild(pro));
        
        container.appendChild(titulo);
        container.appendChild(containerProductos);
        container.appendChild(paginacion());
    };
    render();
    
    const setPagina = (nuevaPagina) => {
        if(nuevaPagina < 0 || nuevaPagina >= getPaginasTotales()){
            return;
        }
        pagina = nuevaPagina;
        render();
    };
    
    const setPrecio = (precio) => {
        if (!tarjetas) return;
        tarjetas.forEach(tarjeta => tarjeta.setPrecio(precio));
    };
    
    return {
        setPrecio,
        setPagina
    };
};

export default ListaProductos;