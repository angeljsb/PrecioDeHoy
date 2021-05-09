/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const EDIT_URL = "/PrecioDolar/api/editarproducto";
const SAVE_URL = "/PrecioDolar/api/guardarproducto";
const DELETE_URL = "/PrecioDolar/api/eliminarproducto";

const callApi = async (url, params = {}) => {
    return await fetch(
        url,
        {
            method: "POST",
            body: JSON.stringify(params),
            headers: {
                "Content-Type": "application/json; charset=UTF-8"
        }
    });
};

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

const getUserData = () => {
    return {
        user_id: window.PrecioDeHoy.usuario.id,
        auth_code: window.PrecioDeHoy.usuario.authCode
    };
};

const editProductDB = (product, on = {}) => {
    const data = {
        ...product,
        ...getUserData()
    };
    callApiEffects(EDIT_URL, data, on);
};

const saveProductDB = (product, on = {}) => {
    const data = {
        ...product,
        ...getUserData()
    };
    callApiEffects(SAVE_URL, data, on);
};

const deleteProductDB = (productId, on = {}) => {
    const data = {
        producto_id: productId,
        ...getUserData()
    };
    callApiEffects(DELETE_URL, data, on);
};

export { editProductDB, saveProductDB, deleteProductDB };
