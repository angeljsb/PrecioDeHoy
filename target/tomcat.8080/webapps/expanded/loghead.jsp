<%-- 
    Document   : loghead
    Created on : 21/02/2021, 03:10:06 PM
    Author     : Angel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.preciodehoy.preciodehoy.backend.AdministradorRecursos" %>
<!DOCTYPE html>
<html>
    <head>
        <%
            String url = request.getRequestURI();
        %>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Precio de hoy - <%= (url.equals(AdministradorRecursos.REGISTRO) ? "Registro" : "Iniciar Sesión") %></title>
        <link rel="stylesheet" href="Recursos/index.css" type="text/css"/>
        <link rel="StyleSheet" type="text/css" href="Recursos/login.css" media="screen" >
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="ph-container--full flex-grow">
            <div class="ph-log__container ph-log__container--primary" >