<%-- 
    Document   : checkboxes
    Created on : 08/05/2021, 12:53:46 PM
    Author     : Angel
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String simbolo = request.getParameter("simbolo");
    String nombre = request.getParameter("nombre");
%>
<div>
    <input type="radio" name="precio-actual" id="check-<%= simbolo %>" data-symbol="<%= simbolo %>" >
    <label for="check-<%= simbolo %>" ><%= nombre %></label>
</div>
