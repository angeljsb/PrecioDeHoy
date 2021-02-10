/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let Global = function(precios) {
    this.preciosProveedores = {};
    precios.forEach((precioActual)=>{
        this.preciosProveedores[precioActual.proveedor.simbolo] = precioActual.precio.numero;
    });
    
    this.precio = Object.values(this.preciosProveedores)[0];
    
    mostrarPrecios(precios);
};

Global.prototype = {
    constructor: Global,
    set actual(simbolo){
        this.precio = this.preciosProveedores[simbolo];
        if(tablaProductos){
            tablaProductos.procesarCambioPrecio();
        }
        convertir();
    }
};

function mostrarPrecios(precios){
          
    let contenedor = document.getElementById("contenedor-tazas");
    let checkboxes = document.getElementById("checkboxes");
        
    for(let precio of precios){

        let divSup = document.createElement("div");
        divSup.className = "ph-taxes__card";
        let link = document.createElement("a");
        let symbol = document.createElement("div");
        symbol.className = "proveedor-symbol";
        let titulo = document.createElement("h3");
        titulo.style = "color: white;";
        symbol.appendChild(titulo);
        link.appendChild(symbol);
        divSup.appendChild(link);
        let precioDiv = document.createElement("div");
        precioDiv.className = "proveedor-precio";
        divSup.appendChild(precioDiv);

        link.href = precio.proveedor.url;
        link.target = "_blank";
        precioDiv.innerHTML = precio.precio.texto;
        titulo.innerHTML = precio.proveedor.simbolo;
        symbol.style = "background-image: linear-gradient(to bottom right, #" 
                + precio.proveedor.colorAsociado.toString(16) + ", linen);";
        divSup.title = precio.proveedor.nombre;

        contenedor.appendChild(divSup);
        
        let radioBox = document.createElement("input");
        radioBox.type = "radio";
        radioBox.id = precio.proveedor.simbolo;
        radioBox.name = "precio-actual";
        radioBox.onclick = ()=>{ global.actual = precio.proveedor.simbolo; };
        
        let label = document.createElement("label");
        label.htmlFor = radioBox.id;
        label.innerHTML = precio.proveedor.nombre;
        
        checkboxes.appendChild(radioBox);
        checkboxes.appendChild(label);
        checkboxes.appendChild(document.createElement("br"));
        
    }
        
}


let inverso = false;

function cambiar(){
    const input = document.getElementById("conversion-input");
    const resultado = document.getElementById("conversion-result");
    input.value = resultado.value = "0";
    
    const dolar = document.getElementById("simbolo-dolar");
    const bolivar = document.getElementById("simbolo-bolivar");
    const claseDolar = "dolar-cambiado";
    const claseBolivar = "bolivar-cambiado";
    
    let cssDolar = dolar.getAttribute("class").toString().split(" ");
    let cssBolivar = bolivar.getAttribute("class").toString().split(" ");
    if(inverso){
        let i = cssDolar.indexOf(claseDolar);
        if(i>0){
            cssDolar.splice(i, 1);
        }
        i = cssBolivar.indexOf(claseBolivar);
        if(i>0){
            cssBolivar.splice(i, 1);
        }
    } else{
        cssDolar.push(claseDolar);
        cssBolivar.push(claseBolivar);
    }
    dolar.setAttribute("class", cssDolar.join(" "));
    bolivar.setAttribute("class", cssBolivar.join(" "));
    inverso = !inverso;
}

function convertir(){
    const input = document.getElementById("conversion-input");
    const resultado = document.getElementById("conversion-result");
    
    if(input.value === ""){
        return;
    }
    if(/^0+./g.test(input.value)){
        while(input.value.charAt(0)==='0'){
            input.value = input.value.substring(1);
        }
    }
    
    let entrada = JSON.parse(input.value);
    
    resultado.value = inverso ? `${entrada / global.precio}` : `${(entrada * global.precio).toFixed(2)}`;
}