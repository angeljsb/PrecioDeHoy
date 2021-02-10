/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

function logout(e){
    e.preventDefault();
    const formulario = document.getElementById("logout-form");
    
    formulario.submit();
}
