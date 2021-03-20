<%-- 
    Document   : index
    Created on : 15/01/2021, 05:41:47 PM
    Author     : Angel
--%>

<%@page import="backend.Parametro"%>
<%@page import="backend.NoEncontradoException"%>
<%@page import="java.sql.SQLException"%>
<%@page import="backend.AdministradorRecursos"%>
<%@page import="backend.TablaUsuario"%>
<%@page import="backend.Usuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%!
    public Usuario revisar(Cookie[] cookies, Usuario actual) {

        if (cookies == null) {
            return new Usuario();
        }

        Cookie id = null,
                auth = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_id")) {
                id = cookie;
            }
            if (cookie.getName().equals("auth_code")) {
                auth = cookie;
            }
        }

        if (id == null || auth == null) {
            return new Usuario();
        }

        TablaUsuario tu = new TablaUsuario();
        try {
            Usuario usuario = tu.autenticar(Integer.parseInt(id.getValue()), Integer.parseInt(auth.getValue()));
            return usuario;
        } catch (SQLException ex) {
        } catch (NoEncontradoException ex) {
            id.setMaxAge(0);
            auth.setMaxAge(0);
        }

        return new Usuario();
    }
%>

<html>
    <head>
        <% boolean loggeado = false; %>
        <jsp:useBean id="user" class="backend.Usuario" scope="session">
            <%
                //Si el bean no está en uso, se buscan las cookies de autenticación
                Cookie[] datosU = request.getCookies();
                user = revisar(datosU, user);
                if (user.getId() > 0) {
                    //Si se pueden autenticar las cookies, se llenan los valores del bean
%>
            <jsp:setProperty name="user" property="id" value="<%= user.getId()%>"/>
            <jsp:setProperty name="user" property="nombre" value="<%= user.getNombre()%>"/>
            <jsp:setProperty name="user" property="authCode" value="<%= user.getAuthCode()%>"/>
            <jsp:setProperty name="user" property="correo" value="<%= user.getCorreo()%>"/>
            <% } %>
        </jsp:useBean>
        <% loggeado = user.getId() > 0;
            if (loggeado) {
                boolean error = false;
                try {
                    TablaUsuario tu = new TablaUsuario();
                    tu.autenticar(user.getId(), user.getAuthCode());
                } catch (NoEncontradoException ex) {
                    error = true;
                } catch (SQLException ex) {
                    error = true;
                }
                if (error) {
                    loggeado = false;
        %>
        <jsp:setProperty name="user" property="id" value="0"/>
        <jsp:setProperty name="user" property="nombre" value=""/>
        <jsp:setProperty name="user" property="authCode" value="0"/>
        <jsp:setProperty name="user" property="correo" value=""/>
        <%
                }
            }
        %>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <!-- Look of this document is driven by a CSS referenced by an href attribute. See http://www.w3.org/TR/xml-stylesheet/ -->
        <link rel="StyleSheet" type="text/css" href="Recursos/index.css" media="screen" >
        <% if (loggeado) { %>

        <!-- Look of this document is driven by a CSS referenced by an href attribute. See http://www.w3.org/TR/xml-stylesheet/ -->
        <link rel="StyleSheet" type="text/css" href="Recursos/lista_productos.css" media="screen" >
        <script type="text/javascript" src="Recursos/opcionesUsuario.js" ></script>
        <% } %>
        <title>Precio de hoy</title>
        <script type="text/javascript" src="Recursos/index.js" ></script>
    </head>
    <body>       

        <header class="ph-navbar">
            <img src="Recursos/precio-de-hoy-logo.svg" alt="logo" class="ph-navbar__logo"></img>
            
            <div id="barra-opciones">
                <% if (!loggeado) {%>
                <a href="<%= AdministradorRecursos.REGISTRO%>" class="ph-button ph-button--primary">Registrate</a>
                <a href="<%= AdministradorRecursos.INICIO_SECCION%>" class="ph-button ph-button--primary">Iniciar sección</a>
                <% } else {%>
                <div class="ph-popup__container">
                    <button class="ph-button pop-button ph-button--primary">Opciones</button>
                    <div class="ph-popup__popup">
                        <a href="" onclick="logout(event)">Cerrar sección</a>
                    </div>
                    <form method="POST" 
                          action="<%= AdministradorRecursos.CERRAR_SECCION%>" 
                          style="display: none;"
                          id="logout-form"
                          >
                        <input type="hidden" 
                               value="<jsp:getProperty name="user" property="id"/>"
                               name="user_id"
                               >
                    </form>
                </div>
                <% }%>
            </div>
        </header>
        <jsp:getProperty name="user" property="id"/>
        <jsp:getProperty name="user" property="nombre"/>
        <jsp:getProperty name="user" property="authCode"/>
        <jsp:getProperty name="user" property="correo"/>
        <div id="cuerpo">
            <div class="ph-container--flex">
                <div class="ph-taxes__container ph-hidden-left">
                    <span class="ph-taxes__arrow"></span>
                    <div id="contenedor-tazas"></div>
                </div>
                <% if(loggeado){ %>
                <div id="contenedor-formulario" style="width: 50px; height: 50px;" class="ph-form-productos__container ph-expandible ph-expandible--minimized">
                    <form id="nuevo-producto" method="POST" action="api/guardarproducto">
                            <input type="hidden" name="user_id" id="form-id-user" value="<jsp:getProperty name="user" property="id" />" >
                            <input type="hidden" name="auth_code" id="form-auth-user" value="<jsp:getProperty name="user" property="authCode" />" >
                            <div class="ph-container--small ph-expandible__inner">
                                <div class="ph-form__container">
                                    <input type="text" maxlength="50" name="nombre_producto"
                                           placeholder="Nombre del producto"
                                           required id="form-nombre-producto"
                                           class="ph-text-input ph-text-input--primary ph-container--full">
                                </div>
                                <div class="ph-form__container">
                                    <input type="text" maxlength="50" 
                                           placeholder="Marca" name="marca"
                                           id="form-marca-producto"
                                           class="ph-text-input ph-text-input--primary ph-container--full">
                                    <span class="ph-divider"></span>
                                    <input type="text" maxlength="50" 
                                               placeholder="Unidad (Kg, pcs, ...etc)"
                                               id="form-unit-producto" name="unidad"
                                               class="ph-text-input ph-text-input--primary ph-container--full">
                                </div>
                                <div class="ph-form__container">
                                    <input type="number" min="0" step="0.01" 
                                           placeholder="Precio" name="precio"
                                           id="form-precio-producto"
                                           class="ph-text-input ph-text-input--primary">

                                    <select name="moneda" form="nuevo-producto" id="form-moneda" name="moneda"
                                            class="ph-text-input ph-text-input--primary ph-container--small-x">
                                        <option value="Dolar">$</option>
                                        <option value="Bolivar">Bs</option>
                                    </select>
                                </div>
                                <div class="ph-form__container ph-expandible__show-more-container ph-expandible__show-more-container--oculto">
                                    <textarea type="text" maxlength="250"
                                            placeholder="Descripción" name="descripcion"
                                            id="form-descripcion-producto"
                                            class="ph-text-input ph-text-input--primary ph-container--full ph-expandible__show-more-content"
                                            ></textarea>
                                </div>
                                <div class="ph-form__container">
                                    <div class="ph-container--full ph-container--center-text">
                                        <input type="submit" value="Guardar" class="ph-button ph-button--primary">
                                        <button type="button" class="ph-expandible__show-more-btn ph-form__description-btn ph-form__description-btn--show-more"></button>
                                        <button type="button" class="ph-expandible__minimize-btn ph-button ph-button--primary">Cancelar</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                </div>
                <% } %>
                <div id="checkboxes" class="ph-container--medium"></div>
            </div>
            <% if(loggeado){ %>
            <div id="container-productos">
                
            </div>
            
            <% } else { %>
                <div>Aplicación para la conversión de dolares a bolivares</div>
            <% } %>
            <div id="conversion-container" class="ph-conversion">
                <div id="conversion" class="ph-conversion__card">
                    <h3 class="ph-container--center-text ph-text--white">Conversión</h3>
                    <div class="ph-container--small">
                        <input 
                            type="number" 
                            id="conversion-input" 
                            class="ph-text-input ph-text-input--primary"
                            value="0"
                            min="0"
                            onkeyup="convertir()"
                            onclick="convertir()"
                            >
                        <span id="simbolo-dolar" class="ph-conversion__symbol">$</span>
                    </div>
                    <div class="ph-container--small">
                        <input
                            type="text"
                            id="conversion-result" 
                            class="ph-text-input ph-text-input--primary ph-text-input--ignore-disable"
                            value="0"
                            disabled
                            >
                        <span id="simbolo-bolivar" class="ph-conversion__symbol">Bs</span>
                    </div>
                    <button onclick="cambiar()" class="ph-container--small-x ph-button ph-button--small ph-button--primary">Cambiar</button>
                </div>
                
                <script>
                    document.addEventListener('changeprice', (e)=>{convertir();});
                    
                    let global = new Global(<%= AdministradorRecursos.consultarApiLocal(AdministradorRecursos.PRECIO_OFICIAL) %>);
                    document.dispatchEvent(new Event('changeprice'));
                </script>
                
            </div>
            <div id="pagina-central">
                <% if (loggeado) { %>
            <script type="text/javascript" src="Recursos/agregarProductos.js"></script>
            <script>
                const productosUsuario = <%= AdministradorRecursos.consultarApiLocal(
                                AdministradorRecursos.PRODUCTOS_USUARIO, 
                                new Parametro("user_id", user.getId()),
                                new Parametro("auth_code", user.getAuthCode())
                        ) %>;
                            
                addProductos(productosUsuario);                
            </script>

                    <script>
                        /*let tablaProductos = new Tabla("tabla-productos",
                            productosUsuario,
                            borrar
                        );*/
                        
                        
                        async function borrar(id){
                            const userId = <jsp:getProperty name="user" property="id"/>
                            const authCode = <jsp:getProperty name="user" property="authCode"/>
                            
                            let data = "producto_id=" + id + "&user_id=" + userId 
                                    + "&auth_code=" + authCode;
                            
                            let response = await fetch("<%= AdministradorRecursos.BORRAR_PRODUCTO %>",
                            {
                                method: "POST",
                                body: data,
                                headers: {
                                    "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                                }
                            }
                            );
                    
                            if(response.ok){
                                tablaProductos.borrarProducto(JSON.parse(id));
                            }else if(response.status===400){
                                alert("No se pudó borrar el producto\n\
                                Esto puede suceder si se cerró la sección desde\n\
                                otro dispositivo. Recargue la pagina y \n\
                                vualva a intentar");
                            }
                        }
                        
                        function submitSecure() {

                            enviarBD();

                            return false;
                        }

                        async function enviarBD() {
                            const precioDolar = global.precio;

                            const nombre = document.getElementById("form-nombre-producto"),
                                    marca = document.getElementById("form-marca-producto"),
                                    precio = document.getElementById("dolar-nuevo-producto"),
                                    moneda = document.getElementById("form-moneda");

                            let data = {
                                user_id: <jsp:getProperty name="user" property="id"/>,
                                nombre: nombre.value,
                                marca: marca.value,
                                precio: (precio.value !== ""
                                        ? (moneda.value === "Dolar"
                                                ? JSON.parse(precio.value)
                                                : JSON.parse(precio.value) / precioDolar)
                                        : 0),
                                auth_code: <jsp:getProperty name="user" property="authCode"/>
                            };

                            let data1 = "user_id=" + data.user_id + "&nombre=" + data.nombre +
                                    "&marca=" + data.marca + "&precio=" + data.precio +
                                    "&auth_code=" + data.auth_code;

                            let response = await fetch(
                                    "<%= AdministradorRecursos.GUARDAR_PRODUCTO%>",
                                    {
                                        method: "POST",
                                        body: data1,
                                        headers: {
                                            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                                        }
                                    });

                            if (response.ok) {
                                let nuevo = await response.json();
                                tablaProductos.addProducto(new Producto(nuevo.id, nuevo.nombre_producto,
                                        nuevo.marca, nuevo.precio_dolares));
                            } else {
                                alert("No se pudó guardar el producto\n\
                                Esto puede suceder si se cerró la sección desde\n\
                                otro dispositivo. Recargue la pagina y \n\
                                vuelva a intentar");
                            }
                        }
                    </script>

                <% } %>
            </div>
        </div>
        <jsp:include page="footer.html" />
    </body>
</html>
