<%-- 
    Document   : login
    Created on : 16/01/2021, 07:17:31 PM
    Author     : Angel
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="backend.TablaUsuario"%>
<%@page import="backend.Usuario"%>
<%@page import="backend.NoEncontradoException"%>
<%@page import="backend.AdministradorRecursos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:useBean id="user" scope="session" class="backend.Usuario"></jsp:useBean>
        <%
            if(user.getId()>0){
                response.sendRedirect(AdministradorRecursos.HOME_PAGE);
            }
            
            boolean post = request.getMethod().equals("POST");
            String correo = "";
            String pass = "";
            boolean recordar = false;
            String campoError = "";
            
            if(post){
                correo = request.getParameter("correo");
                pass = request.getParameter("pass");
                recordar = request.getParameter("recordar")!=null;
                
                if(correo!=null && pass != null){
                    
                    try{
                        TablaUsuario tu = new TablaUsuario();
                        Usuario nuevo = tu.iniciarSeccion(correo, pass);
                        %>
        
        <jsp:setProperty name="user" property="id" value="<%= nuevo.getId() %>"/>
        <jsp:setProperty name="user" property="nombre" value="<%= nuevo.getNombre() %>"/>
        <jsp:setProperty name="user" property="authCode" value="<%= nuevo.getAuthCode() %>"/>
        <jsp:setProperty name="user" property="correo" value="<%= nuevo.getCorreo() %>"/>
        
        
        <%
                        if(recordar){
                            Cookie userId = new Cookie("user_id", Integer.toString(nuevo.getId()));
                            Cookie code = new Cookie("auth_code", Integer.toString(nuevo.getAuthCode()));
                            userId.setMaxAge(60*60*24*365);
                            code.setMaxAge(60*60*24*365);
                            response.addCookie(userId);
                            response.addCookie(code);
                        }else{
                            Cookie[] cookies = request.getCookies();
                            if(cookies!=null)
                                for(Cookie cookie : cookies){
                                    if(cookie.getName().equals("user_id")
                                            ||cookie.getName().equals("auth_code")){
                                        cookie.setMaxAge(0);
                                    }
                                }
                        }
            
                        response.sendRedirect(AdministradorRecursos.HOME_PAGE);
                    }catch(NoEncontradoException ex){
                        campoError = ex.getCampoError();
                    }catch(SQLException ex){
                        System.out.println(ex.getLocalizedMessage());
                    }
                    
                }
            }
        %>
        <link rel="stylesheet" href="Recursos/login.css" type="text/css"/>
        <link rel="stylesheet" href="Recursos/index.css" type="text/css"/>
        <title>Precio de hoy - Iniciar sección</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <div id="fondo" class="total-size">
            <div id="center-bar" class="total-size">
                <div id="formulario">
                    <form action="<%= AdministradorRecursos.INICIO_SECCION %>" method="POST">
                    <div class="form-element">
                    <label for="email">Email</label><br>
                    <input type="email" id="email" name="correo" required>
                    <% if(post && campoError.equals("correo")) {%>
                    <div class="error-message">
                        El correo electronico no está registrado
                    </div>
                    <% } %>
                    </div>
                    <div class="form-element">
                    <label for="contraseña">Contraseña</label><br>
                    <input type="password" id="contraseña" name="pass" 
                           minlength="8" maxlength="50" required>
                    <% if(post && campoError.equals("contraseña")) {%>
                    <div class="error-message">
                        Error de contraseña
                    </div>
                    <% } %>
                    </div>
                    <div class="form-element">
                    <input type="checkbox" id="recordar" name="recordar" >
                    <label for="recordar">Recordar usuario</label>
                    </div>
                    <div class="form-element">
                        <input type="submit" id="submit-button">
                    </div>
                </form>
                </div>
            </div>
        </div>
    </body>
</html>
