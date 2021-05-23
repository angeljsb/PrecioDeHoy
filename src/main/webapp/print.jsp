<%-- 
    Document   : tabla
    Created on : 20/05/2021, 06:35:29 PM
    Author     : Angel
--%>

<%@page import="com.preciodehoy.preciodehoy.backend.AdministradorRecursos"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.preciodehoy.preciodehoy.backend.IBuscadorMoneda"%>
<%@page import="com.preciodehoy.preciodehoy.backend.NoEncontradoException"%>
<%@page import="com.preciodehoy.preciodehoy.api.PrecioOficial"%>
<%@page import="com.preciodehoy.preciodehoy.beans.Proveedor"%>
<%@page import="com.preciodehoy.preciodehoy.backend.RequestReader"%>
<%@page import="com.preciodehoy.preciodehoy.api.ProductoUsuario"%>
<%@page import="com.preciodehoy.preciodehoy.backend.Producto"%>
<%@page import="com.preciodehoy.preciodehoy.backend.ControlUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:useBean id="user" class="com.preciodehoy.preciodehoy.backend.Usuario" scope="session">
    <%
        user = ControlUsuario.getUsuarioActual(request);
        if (user.getId() > 0) {
            //Si se pueden autenticar las cookies, se llenan los valores del bean
%>
    <jsp:setProperty name="user" property="id" value="<%= user.getId()%>"/>
    <jsp:setProperty name="user" property="nombre" value="<%= user.getNombre()%>"/>
    <jsp:setProperty name="user" property="authCode" value="<%= user.getAuthCode()%>"/>
    <jsp:setProperty name="user" property="correo" value="<%= user.getCorreo()%>"/>
    <% } %>
</jsp:useBean>

<%
    boolean loggeado = user.getId() > 0;

    if (!loggeado) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Debes iniciar"
                + " sesión para realizar esta acción");
        return;
    }

    RequestReader reader = new RequestReader(request);
    double precio = reader.getDouble("precio");
    String simbolo = reader.getString("simbolo");

    if (precio == 0 && simbolo.isEmpty()) {
        response.sendRedirect(AdministradorRecursos.HOME_PAGE);
        return;
    }

    if(precio == 0){
        try {
            Proveedor proveedor = PrecioOficial.getPrecio(simbolo);
            precio = proveedor.getPrecio();
        } catch (NoEncontradoException ex) {
            response.sendRedirect(AdministradorRecursos.HOME_PAGE);
            return;
        }
    }

    final Producto[] productos = ProductoUsuario.getProductos(user.getId());

    DecimalFormat formater = IBuscadorMoneda.FORMATO;

%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Imprimir productos</title>
        <link rel="StyleSheet" type="text/css" href="Recursos/print.css" media="print" >
        <script>
            window.addEventListener("afterprint", close);
            window.addEventListener("DOMContentLoaded", print);
        </script>
    </head>
    <body>
        <header>
            <div>
                <h1>Lista de Precios</h1>
            </div>
            <div class="ph-tabla__info ph-tabla__publicidad">
                <p><jsp:getProperty name="user" property="nombre"/></p>
                <p>
                    <b>Taza: <%=formater.format(precio)%></b> 
                    <%= simbolo.isEmpty() 
                        ? "" 
                        : "(" + simbolo +")"%>
                </p>
            </div>
        </header>
        <section>
            <table>
                <tr class="ph-tabla__row">
                    <th>Producto</th>
                    <th>Precio (USD)</th>
                    <th>Precio (Bs)</th>
                </tr>
                <%  for (int i=0; i<productos.length; i++) {
                    Producto producto = productos[i];
                        String nombre = producto.getNombre();
                        String marca = producto.getMarca();
                        String unidad = producto.getUnidad();
                        String precioDolar = formater.format(producto.getPrecioDolar());
                        String precioBoliar = formater.format(
                                producto.getPrecioDolar() * precio
                        );
                %>
                <tr class="ph-tabla__row<%=(i%2 == 0) ? " ph-tabla__row--unpair" : ""%>">
                    <td>
                        <%= nombre%>
                        <i><%= marca.isEmpty() ? "" : (" - " + marca)%></i>
                        <span><%= unidad.isEmpty() ? "" : " (" + unidad + ")"%></span>
                    </td>
                    <td><%= precioDolar %></td>
                    <td><%= precioBoliar %></td>
                </tr>
                <% } %>
            </table>
        </section>
    </body>
</html>
