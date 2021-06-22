/*
 * @author Angel Suárez
 */

const EDIT_URL = "/api/editarproducto";
const SAVE_URL = "/api/guardarproducto";
const DELETE_URL = "/api/eliminarproducto";

/**
 * Hace una llamada de tipo POST a la api
 * 
 * @param {string} url El url de la api
 * @param {any} params Los parametros que pasar a la api
 * @returns {Response} La respuesta de la api
 */
const callApi = async (url, params = {}) => {
    return await fetch(
        url,
        {
            method: "POST",
            body: JSON.stringify(params),
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
            }
        }
    );
};

/**
 * Hace una llamada asincrona a la api y acepta funciones que ejecutará al
 * finalizar la llamada dependiendo de si esta es exitosa o no
 * 
 * @param {string} url El url de la api
 * @param {type} params Los parametros que pasar a la api
 * @param {type} on Un objeto con dos funciones <ul>
 * <li>success: Para cuando la llamada se ejecuta correctamente</li>
 * <li>error: Para cuando la llamada falla</li>
 * </ul>
 */
const callApiEffects = async (url, params = {}, on = {}) => {
    const res = await callApi(url, params);
    
    const success = on.success;
    const error = on.error;
    
    const resJson = await res.json();
    
    if(res.ok && success){
        success(resJson);
    }else if (error){
        error(resJson);
    }
};

/**
 * Llama al endpoint para editar un producto
 * 
 * @param {Producto} product El producto a editar. Debe incluir el id
 * @param {any} on Objeto con funciones success y error para ejecutar al
 * terminar la llamada
 */
const editProductDB = (product, on = {}) => {
    const data = {
        ...product
    };
    callApiEffects(EDIT_URL, data, on);
};

/**
 * Guarda un producto en la base de datos a traves de una llamada a endpoint
 * 
 * @param {Producto} product El producto a insertar
 * @param {any} on Objeto con funciones success y error para ejecutar al
 * terminar la llamada
 */
const saveProductDB = (product, on = {}) => {
    const data = {
        ...product
    };
    callApiEffects(SAVE_URL, data, on);
};

/**
 * Llama al endpoint para borrar un producto de la base de datos
 * 
 * @param {number} productId El id del producto
 * @param {type} on Objeto con funciones success y error para ejecutar al
 * terminar la llamada
 */
const deleteProductDB = (productId, on = {}) => {
    const data = {
        producto_id: productId
    };
    callApiEffects(DELETE_URL, data, on);
};

export { editProductDB, saveProductDB, deleteProductDB };
