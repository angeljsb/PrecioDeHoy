/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Recibe una función como parametro y la ejecuta. Esta no comprueba que el
 * parametro realmente exista o sea una función así que se recomienda usar
 * con precaución
 * 
 * @param {Function} someExecutable Una función a ejecutar
 * @returns {any} La devolución de la función
 */
const exec = (someExecutable) => someExecutable();

/**
 * Crea un objeto de escucha al que se pueden subscribir y desubscribir
 * funciones y se pueden ejecutar todas llamando el metodo exec
 * 
 * @returns {escucha.utilAnonym$0} El objeto de escucha
 */
const escucha = () => {
    const subscritas = [];
    
    /**
     * Subscribe una función al agente de escucha
     * 
     * @param {Function} funcion La función a subscribir
     */
    const subscribir = (funcion) => {
        subscritas.push(funcion);
    };
    
    /**
     * Desubscribe funciones para que dejen de ejecutarse al activarse este
     * agente de escucha.
     * 
     * @param {Function|boolean} funcion La función a desubscribir. Si no se
     * envía este parametro desubscribe todas las funciones actualmente subscritas
     */
    const desubscribir = (funcion = false) => {
        if(!funcion){
            subscritas.splice(0, subscritas.length);
            return;
        }
        const i = subscritas.indexOf(funcion);
        
        if (i===-1) return;
        
        subscritas.splice(i, 1);
    };
    
    /**
     * Ejecuta todas las funciones subscritas pasandoles un parametro
     * 
     * @param {any} param El parametro que pasar a todas las funciones
     */
    const exec = (param) => {
        const ejecutar = (funcion) => funcion(param);
        subscritas.forEach(ejecutar);
    };
    
    return {
        subscribir,
        desubscribir,
        exec
    };
};

/**
 * Crea un HTMLElement a partir de un string html
 * 
 * @param {type} str El texto en formato html a convertir en un objeto html
 * @returns {HTMLElement} El elemento html correspondiente a la primera etiqueta
 * en abrirse y cerrarse en el string
 */
const html = (str) => {
    const div = document.createElement("div");
    div.innerHTML = str;
    return div.firstElementChild;
};

export { exec, escucha, html };
