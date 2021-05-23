<%-- 
    Document   : registro
    Created on : 16/01/2021, 05:43:35 PM
    Author     : Angel
--%>

<%@page import="java.io.File"%>
<%@page import="java.nio.file.Paths"%>
<%@page import="javax.servlet.annotation.MultipartConfig" %>
<%@page import="com.preciodehoy.preciodehoy.backend.NoEncontradoException"%>
<%@page import="com.preciodehoy.preciodehoy.backend.AdministradorRecursos"%>
<%@page import="java.sql.SQLException"%>
<%@page import="com.preciodehoy.preciodehoy.backend.Usuario"%>
<%@page import="com.preciodehoy.preciodehoy.backend.TablaUsuario"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="loghead.jsp" />
        <jsp:useBean id="user" class="com.preciodehoy.preciodehoy.backend.Usuario" scope="session"></jsp:useBean>
        <%
            if(user.getId()>0){
                response.sendRedirect(AdministradorRecursos.HOME_PAGE);
            }
        %>
        <% 
            boolean post = request.getMethod().equals("POST");
            String nombre = "";
            String correo = "";
            String contrasenna = "";
            String imagen = "";
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
                    tu.insert(nombre, correo, contrasenna, imagen);

                    Usuario creado = tu.iniciarSeccion(correo, contrasenna);

                    if(creado!=null){

                        %>
                        
        <jsp:setProperty name="user" property="id" value="<%= creado.getId() %>"/>
        <jsp:setProperty name="user" property="nombre" value="<%= creado.getNombre() %>"/>
        <jsp:setProperty name="user" property="authCode" value="<%= creado.getAuthCode() %>"/>
        <jsp:setProperty name="user" property="correo" value="<%= creado.getCorreo() %>"/>
        <jsp:setProperty name="user" property="imagen" value="<%= creado.getImagen() %>"/>
        
        
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
        
                <div class="ph-log__form">
                    <form action="<%= AdministradorRecursos.REGISTRO %>"
                          method="POST"
                          onsubmit="return validacion()"
                    >
                    <div class="form-element">
                    <input type="text" 
                           id="name" 
                           name="nombre" 
                           maxlength="50" 
                           class="ph-text-input ph-text-input--terciary"
                           placeholder="Nombre/Nombre del negocio"
                           required
                           <% if(post){ %> value="<%= nombre %>" <% } %> 
                    >
                    </div>
                    <div class="form-element">
                    <input type="email" 
                           id="email" 
                           name="correo"
                           class="ph-text-input ph-text-input--terciary"
                           placeholder="Email"
                           required 
                           <% if(post){ %> value="<%= correo %>" <% } %> >
                    <% if(post && errorStatus.equals("23000")) {%>
                    <div class="error-message">
                        El correo electronico ya está en uso
                    </div>
                    <% } %>
                    </div>
                    <div class="form-element">
                    <input type="password" 
                           id="contraseña" 
                           name="pass" 
                           minlength="8" 
                           maxlength="50" 
                           class="ph-text-input ph-text-input--terciary"
                           placeholder="Contraseña"
                           required
                           <% if(post){ %> value="<%= contrasenna %>" <% } %> >
                    </div>
                    <div class="form-element">
                    <input type="password" 
                           id="confirmacion" 
                           class="ph-text-input ph-text-input--terciary"
                           placeholder="Confirmar contraseña"
                           required
                           <% if(post){ %> value="<%= contrasenna %>" <% } %> >
                    <div class="error-message" id="error-confirmacion"></div>
                    </div>
                    <div class="form-element">
                    <input type="checkbox" id="recordar" name="recordar" >
                    <label for="recordar">Recordar usuario</label>
                    </div>
                    <div class="form-element ph-container--center-text">
                        <input type="submit" 
                               id="submit-button" 
                               class="ph-button ph-button--basic"
                        >
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
<jsp:include page="logfoot.jsp" />