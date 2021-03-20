/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

const formularioProductos = document.getElementById("nuevo-producto"),
        contenedorFormulario = document.getElementById("contenedor-formulario"),
        contenedorListaProductos = document.getElementById("container-productos"),
        userId = formularioProductos["form-id-user"].value,
        authCode = formularioProductos["form-auth-user"].value,
        productos = [];

const paginacion = {
    pagina: 0,
    productosPorPagina: 10,
    get productosMostrados() {
        return productos.slice(this.pagina*this.productosPorPagina, 
            (this.pagina*this.productosPorPagina)+this.productosPorPagina);
    }
};

const edit = {
    status: false,
    init: function(id){
        if(!this.status){
            const inputId = document.createElement("input");
            inputId.type = "hidden";
            inputId.id = "form-id-producto";
            inputId.value = id;
            formularioProductos.appendChild(inputId);
        }else{
            const inputId = formularioProductos["form-id-producto"];
            inputId.value = id;
        }
        this.status = true;
    },
    stop: function(){
        if(this.status){
            formularioProductos.removeChild(formularioProductos["form-id-producto"]);
            this.status = false;
        }
    }
};

formularioProductos.addEventListener('submit', async (e) => {
    e.preventDefault();

    const precioDolar = global.precio;

    const nombre = formularioProductos["form-nombre-producto"],
        marca = formularioProductos["form-marca-producto"],
        unidad = formularioProductos["form-unit-producto"],
        precio = formularioProductos["form-precio-producto"],
        descripcion = formularioProductos["form-descripcion-producto"],
        moneda = formularioProductos["form-moneda"];

    const data = {
        user_id: userId,
        nombre_producto: nombre.value,
        marca: marca.value,
        unidad: unidad.value,
        precio: (precio.value !== ""
                ? (moneda.value === "Dolar"
                        ? JSON.parse(precio.value)
                        : JSON.parse(precio.value) / precioDolar)
                : 0),
        descripcion: descripcion.value,
        auth_code: authCode
    };
    
    if(edit.status){
        data.producto_id = formularioProductos["form-id-producto"].value;
    }
    
    const linkToFetch = edit.status ? "/PrecioDolar/api/editarproducto" : "/PrecioDolar/api/guardarproducto"; 

    let response = await fetch(
            linkToFetch,
            {
                method: "POST",
                body: JSON.stringify(data),
                headers: {
                    "Content-Type": "application/json; charset=UTF-8"
                }
            });

    if(response.ok){
        const nuevo = await response.json();
        if(edit.status){
            const indice = productos.findIndex(producto => producto.id === nuevo.id);
            productos.splice(indice, 1);
            edit.stop();
        }
        addProductos(nuevo);

        formularioProductos.reset();
    }
});

const erase = async (id) => {
    let data = "producto_id=" + id + "&user_id=" + userId 
            + "&auth_code=" + authCode;

    let response = await fetch("/PrecioDolar/api/eliminarproducto",
    {
        method: "POST",
        body: data,
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
        }
    }
    );

    if(response.ok){
        const index = productos.findIndex((producto) => producto.id === id);
        productos.splice(index, 1);
        contenedorListaProductos.dispatchEvent(new Event('changeproducts'));
    }else if(response.status===400){
        alert("No se pudó borrar el producto\n\
        Esto puede suceder si se cerró la sección desde\n\
        otro dispositivo. Recargue la pagina y \n\
        vualva a intentar");
    }
};

const presionarEditar = (id) => {
    const productoEdit = productos.find(producto => producto.id === id);
    
    const nombre = formularioProductos["form-nombre-producto"],
        marca = formularioProductos["form-marca-producto"],
        unidad = formularioProductos["form-unit-producto"],
        precio = formularioProductos["form-precio-producto"],
        descripcion = formularioProductos["form-descripcion-producto"];
    
    nombre.value = productoEdit.nombre_producto;
    marca.value = productoEdit.marca ? productoEdit.marca : "";
    unidad.value = productoEdit.unidad ? productoEdit.unidad : "";
    precio.value = productoEdit.precio_dolares ? productoEdit.precio_dolares : "";
    descripcion.value = productoEdit.descripcion ? productoEdit.descripcion : "";
    
    edit.init(productoEdit.id);
    
    contenedorFormulario.click();
};

contenedorListaProductos.addEventListener('changeproducts', (e) => {
    if(productos.length===0){
        return;
    }
    
    let htmlCompleto = '<h2 class="ph-container--center-text">Productos</h2>';

    htmlCompleto += '<div class="ph-container--grid">';

    paginacion.productosMostrados.forEach((producto)=>{
        htmlCompleto += `
        <div>
                <div class="ph-card">
                    <h3 class="ph-card__title">
                        ${producto.nombre_producto}
                    </h3>
                    <span>`;
        if(producto.marca && producto.marca!=="null"){
            htmlCompleto += `${producto.marca} `;
        }
        if(producto.unidad && producto.unidad!=="null"){
            htmlCompleto += `(${producto.unidad})`;
        }
        htmlCompleto += `</span>`;
        if(producto.descripcion && producto.descripcion!=="null"){
            htmlCompleto += `<p class="ph-card__descripcion">${producto.descripcion}</p>`;
        }
        if(producto.precio_dolares && producto.precio_dolares!=="null"){
            htmlCompleto += `<table class="ph-container--small-y">
                        <tbody>
                            <tr>
                                <td class="ph-card__moneda">$</td>
                                <td>${producto.precio_dolares.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2})}</td>
                            </tr>
                            <tr><td class="ph-card__moneda">Bs</td><td id="precio-bolivar-${producto.id}"></td></tr>
                        </tbody>
                    </table>`;
        }
        htmlCompleto += `<div class="ph-card__acciones ph-container--center-text">
                        <button id="edit-${producto.id}" class="ph-button ph-button--small ph-button--terciary">Editar</button>
                        <button id="delete-${producto.id}" class="ph-button ph-button--small ph-button--primary">Borrar</button>
                    </div>
                </div>
            </div>`;
    });
    htmlCompleto += '</div>';

    contenedorListaProductos.innerHTML = htmlCompleto;
    
    paginacion.productosMostrados.forEach((producto)=>{
        const botonDelete = document.getElementById(`delete-${producto.id}`),
        botonEdit = document.getElementById(`edit-${producto.id}`);
        
        botonDelete.addEventListener('click', (e)=>erase(producto.id));
        botonEdit.addEventListener('click', (e)=>presionarEditar(producto.id));
    });
    
    cambiarPrecios();
});

const cambiarPrecios = (e = new Event('')) => {
    paginacion.productosMostrados.forEach((producto)=>{
        if(productos.includes(producto)){
            const marco = document.getElementById(`precio-bolivar-${producto.id}`);
            if(!marco) return;
            const precioBolivar = producto.precio_dolares * global.precio;

            marco.innerHTML = precioBolivar.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2});
        }
    });
};
document.addEventListener('changeprice', cambiarPrecios);

let Ocultable = function(){
    
    const ocultable = contenedorFormulario;
    
    const showMoreBtns = ocultable.querySelectorAll(".ph-expandible__show-more-btn");
    
    const showMoreCont = ocultable.querySelector(".ph-expandible__show-more-container");
    const showMoreContenido = showMoreCont.querySelector(".ph-expandible__show-more-content");
    
    const minimizeBtn = ocultable.querySelectorAll(".ph-expandible__minimize-btn");
    
    const innerDiv = ocultable.querySelector(".ph-expandible__inner");
    
    const expandir = function(e){
        
        const classes = ocultable.className.split(" ");
        
        const remove = classes.indexOf("ph-expandible--minimized");
        classes.splice(remove, 1);
        
        ocultable.className = classes.join(" ");
        
        ocultable.style.width = "calc(100% - 40px)";
        ocultable.style.height = (innerDiv.offsetHeight-33) + "px";
        
        ocultable.removeEventListener('click', expandir);
        
        setTimeout(()=>{
            ocultable.style = {};
        }, 400);
        
    };
    
    const minimizar = function(e){
        
        const classes = ocultable.className.split(" ");
        
        if(classes.includes("ph-expandible--minimized")){
            return;
        }
        
        edit.stop();
        
        formularioProductos.reset();
        
        ocultable.style.width = "calc(100% - 40px)";
        ocultable.style.height = innerDiv.offsetHeight + "px";
        
        classes.push("ph-expandible--minimized");

        ocultable.className = classes.join(" ");
        
        setTimeout(()=>{ocultable.style.width = "50px";
        ocultable.style.height = "50px";}, 10);
        
        setTimeout(()=>{
            ocultable.addEventListener('click', expandir);
        }, 400);
    };
    
    const showMore = function(e){
        showMoreContenido.style.height = "34px";
        
        showMoreCont.style.height = null;
        
        const classes = showMoreCont.className.split(" ");
        
        const remove = classes.indexOf("ph-expandible__show-more-container--oculto");
        classes.splice(remove, 1);
        
        classes.push("ph-expandible__show-more-container--transition");
        
        showMoreCont.className = classes.join(" ");
        
        setTimeout(()=>{
            const remove = classes.indexOf("ph-expandible__show-more-container--transition");
            classes.splice(remove, 1);
            
            classes.push("ph-expandible__show-more-container--mostrado");
            
            showMoreCont.className = classes.join(" ");
            
            showMoreBtns.forEach(btn => {
                btn.removeEventListener('click', showMore);
                btn.addEventListener('click', showLess);
                
                const smbClasses = btn.className.split(" ");
                const remove2 = classes.indexOf("ph-form__description-btn--show-more");
                smbClasses.splice(remove2, 1);
                smbClasses.push("ph-form__description-btn--show-less");
                btn.className = smbClasses.join(" ");
            });
        },400);
    };
    
    const showLess = function(e){
        showMoreCont.style.height = showMoreCont.offsetHeight + "px";
        
        setTimeout(()=>{
        
            showMoreCont.style.height = "0px";

            setTimeout(()=>{

                const classes = showMoreCont.className.split(" ");

                const remove = classes.indexOf("ph-expandible__show-more-container--mostrado");
                classes.splice(remove, 1);

                classes.push("ph-expandible__show-more-container--oculto");

                showMoreCont.className = classes.join(" ");
                
                showMoreBtns.forEach(btn =>{ 
                    btn.removeEventListener('click', showLess);
                    btn.addEventListener('click', showMore);
                    
                    const smbClasses = btn.className.split(" ");
                    const remove2 = classes.indexOf("ph-form__description-btn--show-less");
                    smbClasses.splice(remove2, 1);
                    smbClasses.push("ph-form__description-btn--show-more");
                    btn.className = smbClasses.join(" ");
                });
            }, 400);
            
        }, 10);
    };
    
    ocultable.addEventListener('click', expandir);
    
    contenedorListaProductos.addEventListener('changeproducts', minimizar);
    
    showMoreBtns.forEach(btn => btn.addEventListener('click', showMore));
    
    minimizeBtn.forEach(btn => btn.addEventListener('click', minimizar));
};

Ocultable();

const addProductos = function(nuevos){
    const newArray = [].concat(nuevos);
    productos.reverse();
    productos.push(...newArray);
    productos.reverse();
    
    contenedorListaProductos.dispatchEvent(new Event('changeproducts'));
};
