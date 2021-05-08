/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const showMore = (container, button) => {

    const isOculto = () => container.classList.contains("ph-expandible__show-more-container--oculto");

    const cambiarBoton = (open) => {
        const smbClasses = button.classList;
        smbClasses.toggle("ph-form__description-btn--show-more", !open);
        smbClasses.toggle("ph-form__description-btn--show-less", open);
    };

    const show = () => {
        container.firstElementChild.style.height = "34px";
        container.style.height = null;

        const classes = container.classList;
        classes.add("ph-expandible__show-more-container--transition");
        classes.remove("ph-expandible__show-more-container--oculto");
    };

    const close = () => {
        container.style.height = container.offsetHeight + "px";

        setTimeout(() => container.style.height = "0px", 0);
    };

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
