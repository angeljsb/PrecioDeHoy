<%-- 
    Document   : index
    Created on : 15/01/2021, 05:41:47 PM
    Author     : Angel
--%>

<%@page import="beans.Proveedor"%>
<%@page import="api.PrecioOficial"%>
<%@page import="api.ProductoUsuario"%>
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

<%
    Proveedor[] proveedores = PrecioOficial.getPrecios(null);
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
        <script>
            window.PrecioDeHoy = <%= PrecioOficial.proveedoresToJson(proveedores) %>;
        </script>
        <% if (loggeado) { %>
        <script>
            window.PDHProductos = <%=ProductoUsuario.toJson(ProductoUsuario.getProductos(user.getId(), user.getAuthCode())) %>;
        </script>

        <!-- Look of this document is driven by a CSS referenced by an href attribute. See http://www.w3.org/TR/xml-stylesheet/ -->
        <link rel="StyleSheet" type="text/css" href="Recursos/lista_productos.css" media="screen" >
        <script type="module" src="Recursos/js/logged/render.js"></script>
        <script type="text/javascript" src="Recursos/opcionesUsuario.js" ></script>
        <% } %>
        <title>Precio de hoy</title>
        <script type="module" src="Recursos/js/common/render.js" ></script>
    </head>
    <body>
        
        <jsp:include page="header.jsp" />

        <div id="cuerpo">
            <section id="taxes">
                <div class="ph-taxes__container ph-hidden-left">
                    <span class="ph-taxes__arrow"></span>
                    <div id="contenedor-tazas">
                        <% for(Proveedor prov : proveedores) { %>
                        <jsp:include page="/tax.jsp">
                            <jsp:param name="nombre" value="<%= prov.getNombreProveedor() %>" />
                            <jsp:param name="simbolo" value="<%= prov.getSimbolo() %>" />
                            <jsp:param name="precio" value="<%= prov.getPrecioTexto() %>" />
                            <jsp:param name="url" value="<%= prov.getUrl() %>" />
                            <jsp:param name="color" value="<%= Integer.toHexString( prov.getColor() ) %>" />
                        </jsp:include>
                        <% } %>
                    </div>
                </div>
            </section>
            <section id="products">
                <div class="ph-container--flex">

                    <% if (loggeado) { %>
                    <div id="contenedor-formulario" style="width: 50px; height: 50px;" class="ph-form-productos__container ph-expandible ph-expandible--minimized">
                        <div id="add-icon"><svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 0 24 24" width="24px" fill="#FFFFFF"><path d="M0 0h24v24H0z" fill="none"/><path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/></svg></div>
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
                                <div id="show-more-container" class="ph-form__container ph-expandible__show-more-container ph-expandible__show-more-container--oculto">
                                    <textarea type="text" maxlength="250"
                                              placeholder="Descripción" name="descripcion"
                                              id="form-descripcion-producto"
                                              class="ph-text-input ph-text-input--primary ph-container--full ph-expandible__show-more-content"
                                              ></textarea>
                                </div>
                                <div class="ph-form__container">
                                    <div class="ph-container--full ph-container--center-text">
                                        <input type="submit" value="Guardar" class="ph-button ph-button--primary">
                                        <button type="button" id="show-more-button" class="ph-expandible__show-more-btn ph-form__description-btn ph-form__description-btn--show-more"></button>
                                        <button type="button" class="ph-expandible__minimize-btn ph-button ph-button--primary">Cancelar</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                    <% } %>
                    <div id="checkboxes" class="ph-container--medium">
                        <% for(Proveedor prov : proveedores) { %>
                        <jsp:include page="/checkboxes.jsp" >
                            <jsp:param name="simbolo" value="<%= prov.getSimbolo() %>" />
                            <jsp:param name="nombre" value="<%= prov.getNombreProveedor() %>" />
                        </jsp:include>
                        <% } %>
                    </div>

                </div>
                <% if (loggeado) { %>
                <div id="container-productos">

                </div>

                <% } else { %>
                <h1>Aplicación para la conversión de dolares a bolivares</h1>
                <% }%>
            </section>
            <section id="conversion-container" class="ph-conversion">
                <div id="conversion" class="ph-conversion__card">
                    <h3 class="ph-container--center-text ph-text--white">Conversión</h3>
                    <div class="ph-container--small">
                        <input 
                            type="number"
                            id="conversion-input" 
                            class="ph-text-input ph-text-input--primary ph-conversion__input"
                            value="0"
                            min="0"
                            >
                        <span id="simbolo-dolar" class="ph-conversion__symbol ph-conversion__dolar-symbol">$</span>
                    </div>
                    <div class="ph-container--small">
                        <input
                            type="text"
                            id="conversion-result" 
                            class="ph-text-input ph-text-input--primary ph-text-input--ignore-disable ph-conversion__result"
                            value="0"
                            disabled
                            >
                        <span id="simbolo-bolivar" class="ph-conversion__symbol ph-conversion__bolivar-symbol">Bs</span>
                    </div>
                    <button id="conversion-button" class="ph-container--small-x ph-button ph-button--small ph-button--primary">Cambiar</button>
                </div>

                <!--<script>
                    document.addEventListener('changeprice', (e) => {
                        convertir();
                    });

                    let global = new Global();
                    document.dispatchEvent(new Event('changeprice'));
                </script>-->

            </section>
            <div style="display: none;">
                <% if (loggeado) {%>
                <!--<script type="text/javascript" src="Recursos/agregarProductos.js"></script>-->

                <script>

                    async function borrar(id) {
                        const userId = <jsp:getProperty name="user" property="id"/>
                        const authCode = <jsp:getProperty name="user" property="authCode"/>

                        let data = "producto_id=" + id + "&user_id=" + userId
                                + "&auth_code=" + authCode;

                        let response = await fetch("<%= AdministradorRecursos.BORRAR_PRODUCTO%>",
                                {
                                    method: "POST",
                                    body: data,
                                    headers: {
                                        "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"
                                    }
                                }
                        );

                        if (response.ok) {
                            tablaProductos.borrarProducto(JSON.parse(id));
                        } else if (response.status === 400) {
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

                <% }%>
            </div>
        </div>
        <jsp:include page="footer.html" />
    </body>
</html>
