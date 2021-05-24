<%-- 
    Document   : login
    Created on : 16/01/2021, 07:17:31 PM
    Author     : Angel
--%>

<%@page import="java.sql.SQLException"%>
<%@page import="com.preciodehoy.preciodehoy.backend.TablaUsuario"%>
<%@page import="com.preciodehoy.preciodehoy.backend.Usuario"%>
<%@page import="com.preciodehoy.preciodehoy.backend.NoEncontradoException"%>
<%@page import="com.preciodehoy.preciodehoy.backend.AdministradorRecursos"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<jsp:include page="loghead.jsp" />
        <jsp:useBean id="user" scope="session" class="com.preciodehoy.preciodehoy.backend.Usuario"></jsp:useBean>
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
                <div class="ph-log__form">
                    <form action="<%= AdministradorRecursos.INICIO_SECCION %>" method="POST">
                    <div class="form-element">
                    <input type="email" 
                           id="email" 
                           name="correo" 
                           class="ph-text-input ph-text-input--terciary"
                           placeholder="Email"
                           required
                    >
                    <% if(post && campoError.equals("correo")) {%>
                    <div class="error-message">
                        El correo electronico no está registrado
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
                    >
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
                    <div class="form-element ph-container--center-text">
                        <input type="submit" 
                               id="submit-button" 
                               class="ph-button ph-button--basic"
                        >
                    </div>
                </form>
                </div>
<jsp:include page="logfoot.jsp" />
