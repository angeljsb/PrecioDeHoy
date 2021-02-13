/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

let Producto = function (id, nombre, marca = "", precioDolar = 0) {
    this.id = id;
    this.nombre = nombre;
    this.marca = marca;
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
        console.log(this.formulario);
        
        const nombre = this.formulario["form-nombre-producto"],
            marca = this.formulario["form-marca-producto"],
            unidad = this.formulario["form-unit-producto"],
            precio = this.formulario["form-precio-producto"],
            moneda = this.formulario["form-moneda"];
            
        console.log(nombre, marca, unidad, precio, moneda);
        
//        const data = {
//            user_id: userId,
//            nombre: nombre.value,
//            marca: marca.value,
//            unidad: unidad.value,
//            precio: (precio.value !== ""
//                    ? (moneda.value === "Dolar"
//                            ? JSON.parse(precio.value)
//                            : JSON.parse(precio.value) / precioDolar)
//                    : 0),
//            auth_code: authCode
//        };

//        let data1 = "user_id=" + data.user_id + "&nombre=" + data.nombre +
//                "&marca=" + data.marca + "&precio=" + data.precio +
//                "&auth_code=" + data.auth_code;
//
//        let response = await fetch(
//                "<%= AdministradorRecursos.GUARDAR_PRODUCTO%>",
//                {
//                    method: "POST",
//                    body: data1,
//                    headers: {
//                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
//                    }
//                });
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

let Tabla = function (id, productos = [], borrar ={}, editar={}) {
    this.tabla = document.getElementById(id);
    this.id = id;
    
    this.borrar = borrar;
    this.editar = editar;

    this.productos = [];

    for (let producto of productos) {
        this.addProducto(
                new Producto(producto.id, producto.nombre_producto, producto.marca, producto.precio_dolares)
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
