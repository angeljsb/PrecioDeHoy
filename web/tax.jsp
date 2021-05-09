<%-- 
    Document   : tax
    Created on : 08/05/2021, 02:09:08 PM
    Author     : Angel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String nombre = request.getParameter("nombre");
    String simbolo = request.getParameter("simbolo");
    String precio = request.getParameter("precio");
    String url = request.getParameter("url");
    String color = request.getParameter("color");
%>
<div class="ph-taxes__card" title="<%= nombre %>">
    <a href="<%= url %>" target="_blank">
        <div 
            class="proveedor-symbol" 
            style="background-image: linear-gradient(to bottom right, #<%= color %>, linen);"
        >
            <h3 class="ph-text--white"><%= simbolo %></h3>
        </div>
    </a>
    <div class="proveedor-precio"><%= precio %></div>
</div>