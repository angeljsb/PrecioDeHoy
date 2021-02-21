/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let Producto = function (id, nombre, marca = "", unidad="", descripcion="", precioDolar = 0) {
    this.id = id;
    this.nombre = nombre;
    this.marca = marca;
    this.unidad = unidad;
    this.descripcion = descripcion;
    this.precioDolar = precioDolar;
    this.precioBolivar = precioDolar * global.precio;
};

Producto.prototype.procesarCambioPrecio = function(){
    this.precioBolivar = this.precioDolar * global.precio;
};

Producto.prototype.constructor = Producto;

let FormularioControlado = function(id){
    this.formulario = document.getElementById(id);
    
    this.formulario.addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const precioDolar = global.precio;
        
        const userId = this.formulario["form-id-user"],
            authCode = this.formulario["form-auth-user"],
            nombre = this.formulario["form-nombre-producto"],
            marca = this.formulario["form-marca-producto"],
            unidad = this.formulario["form-unit-producto"],
            precio = this.formulario["form-precio-producto"],
            descripcion = this.formulario["form-descripcion-producto"],
            moneda = this.formulario["form-moneda"];
            
        const data = {
            user_id: userId.value,
            nombre_producto: nombre.value,
            marca: marca.value,
            unidad: unidad.value,
            precio: (precio.value !== ""
                    ? (moneda.value === "Dolar"
                            ? JSON.parse(precio.value)
                            : JSON.parse(precio.value) / precioDolar)
                    : 0),
            descripcion: descripcion.value,
            auth_code: authCode.value
        };

//        let data1 = "user_id=" + data.user_id + "&nombre_producto=" + data.nombre +
//                "&marca=" + data.marca + "&unidad=" + data.unidad +
//                "&precio=" + data.precio + "&descripcion=" + data.descripcion +
//                "&auth_code=" + data.auth_code;
        
        let response = await fetch(
                "/PrecioDolar/api/guardarproducto",
                {
                    method: "POST",
                    body: JSON.stringify(data),
                    headers: {
                        "Content-Type": "application/json"
                    }
                });
        
        if(response.ok){
            console.log(await response.json());
        }
    });
};

FormularioControlado.prototype = {
    constructor: FormularioControlado
};

let Ocultable = function(id){
    
    const ocultable = document.getElementById(id);
    
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
        
        ocultable.style.width = "calc(100% - 40px)";
        ocultable.style.height = innerDiv.offsetHeight + "px";
        
        const classes = ocultable.className.split(" ");
        
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
    
    showMoreBtns.forEach(btn => btn.addEventListener('click', showMore));
    
    minimizeBtn.forEach(btn => btn.addEventListener('click', minimizar));
};

let ListaProductos = function(productos = []){
    this.page = 0;
    this.container = document.getElementById("container-productos");
    this.productos = productos;
};

ListaProductos.prototype = {
    constructor: ListaProductos,
    show: function(){
        if(this.productos.length===0){
            return;
        }
        const itemsPerPage = 10;
        const prodcutosMostrados = this.productos.slice(this.page*itemsPerPage, itemsPerPage);
        
        let htmlCompleto = '<h2 class="ph-container--center-text">Productos</h2>';
        
        htmlCompleto += '<div class="ph-container--grid">';
        
        prodcutosMostrados.forEach((producto)=>{
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
                                <tr><td class="ph-card__moneda">$</td>
<td>${producto.precio_dolares.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2})}</td></tr>
                                <tr><td class="ph-card__moneda">Bs</td><td id="precio-bolivar-${producto.id}"></td></tr>
                            </tbody>
                        </table>`;
            }
            htmlCompleto += `<div class="ph-card__acciones ph-container--center-text">
                            <button class="ph-button ph-button--small ph-button--terciary">Editar</button>
                            <button class="ph-button ph-button--small ph-button--primary">Borrar</button>
                        </div>
                    </div>
                </div>`;
        });
        htmlCompleto += '</div>';
        
        this.container.innerHTML = htmlCompleto;
        
        const cambiar = (e) => {
            prodcutosMostrados.forEach((producto)=>{
                const marco = document.getElementById(`precio-bolivar-${producto.id}`);
                const precioBolivar = producto.precio_dolares * global.precio;
                
                marco.innerHTML = precioBolivar.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2});
            });
        };
        document.addEventListener('changeprice', cambiar);
    }
};

/*let Tabla = function (id, productos = [], borrar ={}, editar={}) {
    this.tabla = document.getElementById(id);
    this.id = id;
    
    this.borrar = borrar;
    this.editar = editar;

    this.productos = [];

    for (let producto of productos) {
        this.addProducto(
                new Producto(producto.id, producto.nombre_producto, producto.marca, "","", producto.precio_dolares)
                );
    }

};

Tabla.prototype = {
    constructor: Tabla,
    tableHeaders: ["Nombre del producto", "Marca", "Precio ($)",
        "Precio (Bs)", "Acciones"],
    addProducto: function (producto) {
        if (this.tabla === null) {
            this.crearTabla();
        }
        let tr = this.tabla.insertRow(1);
        tr.innerHTML = `
                <td>${producto.nombre}</td>
                <td>${producto.marca}</td>
                <td>${producto.precioDolar.toLocaleString(["es"], {minimumFractionDigits: 2, maximumFractionDigits: 2})}</td>
                <td>${producto.precioBolivar.toLocaleString(["es"],{minimumFractionDigits: 2, maximumFractionDigits: 2})}</td>
            `;
        tr.id = "producto-" + producto.id;

        let borrar = document.createElement("button");
        let acciones = document.createElement("td");
        borrar.onclick = ()=>this.borrar(producto.id);
        borrar.innerHTML = "Borrar";
        acciones.appendChild(borrar);
        tr.appendChild(acciones);
        
        this.productos[producto.id] = producto;
    },
    borrarProducto: function (id) {
        let row = document.getElementById("producto-" + id);
        let tablaB = row.parentNode;
        tablaB.removeChild(row);

        this.productos.splice(id, 1);
    },
    crearTabla: function () {

        let container = document.getElementById("contenedor-tabla");
        this.tabla = document.createElement("table");

        this.tabla.id = this.id;
        this.tabla.className = "ph-table";
        let thead = this.tabla.insertRow(0);
        
        this.tableHeaders.forEach((header)=>{
            let th = document.createElement("th");
            th.innerHTML = header;
            thead.appendChild(th);
        });

        container.appendChild(this.tabla);
    },
    procesarCambioPrecio: function(){
        this.productos.forEach(producto => {
            producto.procesarCambioPrecio();
            let tr = document.getElementById("producto-" + producto.id);
            let col = tr.getElementsByTagName("td")[3];
            
            col.innerHTML = producto.precioBolivar.toLocaleString(["es"],{minimumFractionDigits: 2, maximumFractionDigits: 2});
        });
    }
};
*/