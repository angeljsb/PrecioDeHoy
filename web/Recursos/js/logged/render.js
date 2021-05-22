/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Expandible from "./expandible.js";
import showMore from "./show-more.js";
import ListaProductos from "./lista-productos.js";
import FormControl from "./form-control.js";
import Productos from "./productos-manager.js";
import {bloqueadorCantidad} from "./functions.js";
import Imprimir from "./print-link.js";

/**
 * Función que inicializa la funcionalidad de la aplicación para usuarios
 * logueados
 */
const iniciar = () => {
    const elementos = {
        expandibleForm: document.getElementById("contenedor-formulario"),
        showMoreContainer: document.getElementById("show-more-container"),
        showMoreButton: document.getElementById("show-more-button"),
        productsContainer: document.getElementById("container-productos"),
        addForm: document.getElementById("nuevo-producto"),
        printLink: document.getElementById("ph-imprimir-link")
    };
    
    const controladores = {
        expandibleForm: Expandible(elementos.expandibleForm),
        showMore: showMore(elementos.showMoreContainer, elementos.showMoreButton),
        productList:  ListaProductos(elementos.productsContainer),
        formControl: FormControl(elementos.addForm),
        bloqueador: bloqueadorCantidad(elementos.expandibleForm),
        printLink: Imprimir(elementos.printLink)
    };
    
    const Precio = window.PrecioDeHoy.PrecioManager;
    
    Precio.subscribir(controladores.productList.setPrecio);
    Productos.subscribir(controladores.productList.setProductos);
    
    window.PrecioDeHoy.controladoresUsuario = controladores;
};

window.addEventListener("DOMContentLoaded", iniciar);
