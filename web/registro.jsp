<%-- 
    Document   : registro
    Created on : 16/01/2021, 05:43:35 PM
    Author     : Angel
--%>

<%@page import="backend.NoEncontradoException"%>
<%@page import="backend.AdministradorRecursos"%>
<%@page import="java.sql.SQLException"%>
<%@page import="backend.Usuario"%>
<%@page import="backend.TablaUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <jsp:useBean id="user" class="backend.Usuario" scope="session"></jsp:useBean>
        <%
            if(user.getId()>0){
                response.sendRedirect(AdministradorRecursos.HOME_PAGE);
            }
        %>
        <link rel="stylesheet" href="Recursos/index.css" type="text/css"/>
        <link rel="StyleSheet" type="text/css" href="Recursos/login.css" media="screen" >
        <title>Precio de hoy - Registro</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <% 
            boolean post = request.getMethod().equals("POST");
            String nombre = "";
            String correo = "";
            String contrasenna = "";
            boolean recordar = false;
            String errorStatus = "";
            if(post){
                nombre = request.getParameter("nombre");
                correo = request.getParameter("correo");
                contrasenna = request.getParameter("pass");
                recordar = request.getParameter("recordar")!=null;
                
                if(nombre!=null&&correo!=null&&contrasenna!=null)
                try{
                    TablaUsuario tu = new TablaUsuario();
                    tu.insert(nombre, correo, contrasenna);

                    Usuario creado = tu.iniciarSeccion(correo, contrasenna);

                    if(creado!=null){

                        %>
                        
        <jsp:setProperty name="user" property="id" value="<%= creado.getId() %>"/>
        <jsp:setProperty name="user" property="nombre" value="<%= creado.getNombre() %>"/>
        <jsp:setProperty name="user" property="authCode" value="<%= creado.getAuthCode() %>"/>
        <jsp:setProperty name="user" property="correo" value="<%= creado.getCorreo() %>"/>
        
        
        <%
                        if(recordar){
                            Cookie userId = new Cookie("user_id", Integer.toString(creado.getId()));
                            Cookie code = new Cookie("auth_code", Integer.toString(creado.getAuthCode()));
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
                    }
                }catch(SQLException ex){
                    errorStatus = ex.getSQLState();
                }catch(NoEncontradoException ex){
                    System.err.println(ex.getLocalizedMessage());
                }
                
            }
        %>
    </head>
    <body>
        <div id="fondo" class="total-size">
            <div id="center-bar" class="total-size">
                <div id="formulario">
                    <form action="<%= AdministradorRecursos.REGISTRO %>" method="POST" onsubmit="return validacion()">
                    <div class="form-element">
                    <label for="name">Nombre/Nombre del negocio</label><br>
                    <input type="text" id="name" name="nombre" maxlength="50" required
                           <% if(post){ %> value="<%= nombre %>" <% } %> >
                    </div>
                    <div class="form-element">
                    <label for="email">Email</label><br>
                    <input type="email" id="email" name="correo" required 
                           <% if(post){ %> value="<%= correo %>" <% } %> >
                    <% if(post && errorStatus.equals("23000")) {%>
                    <div class="error-message">
                        El correo electronico ya está en uso
                    </div>
                    <% } %>
                    </div>
                    <div class="form-element">
                    <label for="contraseña">Contraseña</label><br>
                    <input type="password" id="contraseña" name="pass" 
                           minlength="8" maxlength="50" required
                           <% if(post){ %> value="<%= contrasenna %>" <% } %> >
                    </div>
                    <div class="form-element">
                    <label for="confirmacion">Confirmar contraseña</label><br>
                    <input type="password" id="confirmacion" required
                           <% if(post){ %> value="<%= contrasenna %>" <% } %> >
                    <div class="error-message" id="error-confirmacion"></div>
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
                <script>
                    function validacion(){
                        const pass = document.getElementById("contraseña");
                        const confirm = document.getElementById("confirmacion");
                        const error = document.getElementById("error-confirmacion");

                        if(pass.value !== confirm.value){
                            error.innerHTML = "¡La validación de contraseña no coincide!";
                            return false;
                        }

                        return true;
                    }
                </script>
            </div>
        </div>
    </body>
</html>