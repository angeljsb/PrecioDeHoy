<%-- 
    Document   : header.jsp
    Created on : 21/02/2021, 02:41:13 PM
    Author     : Angel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="backend.AdministradorRecursos"%>
<!DOCTYPE html>
<jsp:useBean id="user" class="backend.Usuario" scope="session"></jsp:useBean>
<% 
    boolean loggeado = user.getId()>0;  
    String url = request.getRequestURI();
%>
<header>
    <a href="<%= AdministradorRecursos.HOME_PAGE %>">
    <img src="Recursos/precio-de-hoy-logo.svg" alt="logo" class="ph-navbar__logo">
    </a>
    
    <% if(!(url.equals(AdministradorRecursos.INICIO_SECCION)
            || url.equals(AdministradorRecursos.REGISTRO))){ %>
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
    <% } %>
    </div>
    <% } %>
</header>
