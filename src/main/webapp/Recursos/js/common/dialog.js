/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import { html } from "./util.js";

const Dialog = (config = {}) => {
    const parent = document.body;
    
    const element = html(
        `<section class="ph-modal__container" >
            <div class="ph-modal__back" ></div>
            <div class="ph-modal">
                <div class="ph-modal__body" >
                    <div class="ph-modal__title" ></div>
                    <div class="ph-modal__content" ></div>
                    <div class="ph-modal__options" ></div>
                </div>
            </div>
        </section>`
    );
    const getContainerOf = (part) => element.querySelector(`.ph-modal__${part}`);
    const titleElement = () => getContainerOf("title");
    const contentElement = () => getContainerOf("content");
    const optionsElement = () => getContainerOf("options");
    
    const isOpen = () => element.parentElement === parent;
    
    const open = () => {
        if (isOpen()) return;
        parent.appendChild(element);
    };
    
    const close = () => {
        if (!isOpen()) return;
        parent.removeChild(element);
    };
    
    const setOpen = (op) => op ? open() : close();
    
    const createOptionButton = (option) => {
        const texto = option.texto || "Opción";
        const accion = option.accion;
        const type = option.type || "primary";
        
        const boton = html(`
            <button class="ph-button ph-button--${type}" >
                ${texto}
            </button>
        `);
        
        accion && boton.addEventListener("click", accion);
        
        return boton;
    };
    
    const setContent = (content) => {
        contentElement().innerHTML = "";
        contentElement().appendChild(content);
    };
    
    const setTitle = (title) => {
        titleElement().innerHTML = `<h3>${title}</h3>`;
    };
    
    const setOptions = (options) => {
        optionsElement().innerHTML = "";
        const append = (el) => optionsElement().appendChild(el);
        options.map(createOptionButton).forEach(append);
    };
    
    getContainerOf("back").addEventListener("click", close);
    config.content && setContent(config.content);
    config.title && setTitle(config.title);
    config.options && setOptions(config.options);
    
    return {
        open,
        close,
        isOpen,
        setOpen,
        setTitle,
        setContent,
        setOptions
    };
    
};

export default Dialog;
