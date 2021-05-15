/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.ControlUsuario;
import backend.NoEncontradoException;
import backend.RequestReader;
import backend.TablaProducto;
import backend.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;

/**
 * Endpoint que elimina un producto de la base de datos y devuelve un objeto
 * con el id del producto se la solicitud se realizó con exito
 *
 * @author Angel
 * @since v1.0.0
 */
@WebServlet(name = "BorrarProducto", urlPatterns = {"/api/eliminarproducto"})
public class BorrarProducto extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     * @since v1.0.0
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Usuario user = ControlUsuario.getUsuarioActual(request);
        RequestReader reader = new RequestReader(request);
        
        if(user == null){
            response.sendError(403, "Debes estár logueado para realizar esta acción");
            return;
        }
        
        int productoId;
        int userId;
        
        try{
            productoId = reader.getInt("producto_id");
            userId = user.getId();

            if(productoId==0){
                response.sendError(400, "El id del producto es un parametro obligatorio");
                return;
            }
            
            TablaProducto tp = new TablaProducto();
            tp.borrarProducto(productoId, userId);
            
        }catch(SQLException|NoEncontradoException|NumberFormatException ex){
            response.sendError(400, ex.getMessage());
            return;
        }
        
        response.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter out = response.getWriter()) {
            out.println("{"
                    + "\"id\":" 
                    + productoId
                    + "}");
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
