/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Recibe una función como parametro y la ejecuta. sta no comprueba que el
 * parametro realmente exista o sea una función así que se recomienda usar
 * con precaución
 * 
 * @param {Function} someExecutable Una función a ejecutar
 * @returns {any}
 */
const exec = (someExecutable) => someExecutable();

const escucha = () => {
    const subscritas = [];
    
    const subscribir = (funcion) => {
        subscritas.push(funcion);
    };
    
    const desubscribir = (funcion = false) => {
        if(!funcion){
            subscritas.splice(0, subscritas.length);
            return;
        }
        const i = subscritas.indexOf(funcion);
        subscritas.splice(i, 1);
    };
    
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

export { exec, escucha };
