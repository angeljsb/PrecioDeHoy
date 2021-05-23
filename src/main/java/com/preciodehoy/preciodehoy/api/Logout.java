/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.api;

import com.preciodehoy.preciodehoy.backend.AdministradorRecursos;
import com.preciodehoy.preciodehoy.backend.ControlUsuario;
import com.preciodehoy.preciodehoy.backend.NoEncontradoException;
import com.preciodehoy.preciodehoy.backend.TablaUsuario;
import com.preciodehoy.preciodehoy.backend.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_NO_CONTENT;

/**
 * Cierra la sesión de un usuario y redirecciona a index.jsp
 *
 * @author Angel
 * @since v1.0.0
 */
@WebServlet(name = "Logout", urlPatterns = {"/logout"})
public class Logout extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Usuario user = ControlUsuario.getUsuarioActual(request);

        if(user==null || user.getId() == 0){
            response.sendError(SC_UNAUTHORIZED, "Debes estár logueado para realizar esta acción");
            response.sendRedirect(AdministradorRecursos.HOME_PAGE);
            return;
        }
        
        //Cambio el codigo de autenticación en la base de datos
        try {
            int idInt = user.getId();
            
            TablaUsuario tu = new TablaUsuario();
            tu.cerrarSeccion(idInt);
        } catch (SQLException|NoEncontradoException|NumberFormatException ex) {
            response.sendError(SC_BAD_REQUEST, ex.getMessage());
            return;
        }
        
        //Nulifico el atributo user
        request.getSession().setAttribute("user", null);
        
        //Elimino las cookies de autenticación
        for(Cookie cookie : request.getCookies()){
            String name = cookie.getName();
            if(name.equals("user_id") || name.equals("auth_code")){
                cookie.setMaxAge(0);
            }
        }
        
        response.setStatus(SC_NO_CONTENT);
        response.sendRedirect(AdministradorRecursos.HOME_PAGE);
        response.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("{}");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
