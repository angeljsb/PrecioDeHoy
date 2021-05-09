/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const EDIT_URL = "/PrecioDolar/api/editarproducto";
const SAVE_URL = "/PrecioDolar/api/guardarproducto";

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

const editProductDB = (product, on = {}) => {
    callApiEffects(EDIT_URL, product, on);
};

const saveProductDB = (product, on = {}) => {
    callApiEffects(SAVE_URL, product, on);
};

export { editProductDB, saveProductDB };
