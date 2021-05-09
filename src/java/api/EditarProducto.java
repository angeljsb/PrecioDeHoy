/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.NoEncontradoException;
import backend.Producto;
import backend.RequestReader;
import backend.TablaProducto;
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
 *
 * @author Angel
 */
@WebServlet(name = "EditarProducto", urlPatterns = {"/api/editarproducto"})
public class EditarProducto extends HttpServlet {

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
        
        RequestReader reader = new RequestReader(request);
        
        int userId = reader.getInt("user_id"),
                authCode = reader.getInt("auth_code"),
                productoId = reader.getInt("producto_id");
        String nombre = reader.getString("nombre_producto"),
                marca = reader.getString("marca"),
                unidad = reader.getString("unidad"),
                descripcion = reader.getString("descripcion");
        double precioDolar = reader.getDouble("precio");
        
        if(userId==0||nombre==null||nombre.isEmpty()||authCode==0){
            response.sendError(400);
            return;
        }
        
        Producto ingresado;
        try{
            TablaProducto tp = new TablaProducto();
            ingresado = tp.update(userId, nombre, marca, unidad, descripcion, precioDolar, authCode, productoId);
        }catch(SQLException|NoEncontradoException ex){
            response.sendError(400);
            return;
        }
        
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(ProductoUsuario.toJson(ingresado));
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
