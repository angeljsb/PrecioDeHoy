/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Inicializa el componente que oculta y muestra el input de la descripción
 * 
 * @param {HTMLElement} container El componente que se abre y cierra
 * @param {HTMLElement} button El botón que abre y cierra el componente
 * @returns {any} Conjunto de funciones que abren y cierranel componente
 */
const showMore = (container, button) => {

    /**
     * @returns {boolean} Si el componente está oculto
     */
    const isOculto = () => container.classList.contains("ph-expandible__show-more-container--oculto");

    /**
     * Cambia el icono del boton entre el de mostrar más y el de mostrar menos
     * 
     * @param {boolean} open Si se va a abrir o a cerrar el componente
     */
    const cambiarBoton = (open) => {
        const smbClasses = button.classList;
        smbClasses.toggle("ph-form__description-btn--show-more", !open);
        smbClasses.toggle("ph-form__description-btn--show-less", open);
    };

    /**
     * Muestra el componente oculto
     */
    const show = () => {
        container.firstElementChild.style.height = "34px";
        container.style.height = null;

        const classes = container.classList;
        classes.add("ph-expandible__show-more-container--transition");
        classes.remove("ph-expandible__show-more-container--oculto");
    };

    /**
     * Oculta el componente
     */
    const close = () => {
        container.style.height = container.offsetHeight + "px";

        setTimeout(() => container.style.height = "0px", 0);
    };

    /**
     * Cambia si el componente está abierto o cerrado
     * 
     * @param {"not"|boolean} forceOpen Si se quiere forzar a abrir el componente
     * true para abrir y false para cerrar
     */
    const changeOpen = (forceOpen = "not") => {
        const open = isOculto();
        
        if([true, false].includes(forceOpen) && forceOpen !== open){
            return;
        }

        if (open) {
            show();
        } else {
            close();
        }

        setTimeout(() => {
            const classes = container.classList;
            classes.remove(`ph-expandible__show-more-container--${open ? "transition" : "mostrado"}`);
            classes.add(`ph-expandible__show-more-container--${open ? "mostrado" : "oculto"}`);

            cambiarBoton(open);
        }, 400);
    };
    
    button.addEventListener("click", changeOpen);
    
    return {
        cambiar: changeOpen
    };

};

export default showMore;
