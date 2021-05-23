/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.api;

import com.preciodehoy.preciodehoy.backend.ControlUsuario;
import com.preciodehoy.preciodehoy.backend.Producto;
import com.preciodehoy.preciodehoy.backend.ResponseWriter;
import com.preciodehoy.preciodehoy.backend.TablaProducto;
import com.preciodehoy.preciodehoy.backend.Usuario;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Endpoint que devuelve todos los productos para un usuario
 *
 * @author Angel
 * @since v1.0.0
 */
@WebServlet(name = "ProductoUsuario", urlPatterns = {"/api/productos"})
public class ProductoUsuario extends HttpServlet {
    
    public static Producto[] getProductos(int userId){
        Producto[] productos;
        try {
            TablaProducto tp = new TablaProducto();
            productos = tp.getProductosUsuario(userId);
        } catch (SQLException ex) {
            productos = null;
        }
        return productos;
    };

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
        ResponseWriter writer = new ResponseWriter(response);
        
        if(user==null || user.getId() == 0){
            writer.sendError(SC_UNAUTHORIZED, "Debes estar logueado para realizar esta acción");
            return;
        }
        
        int id = user.getId();
        
        Producto[] productos = getProductos(id);
        
        if(productos == null){
            writer.sendError(SC_BAD_REQUEST, "Hubo un error al pedir a la base de datos"
                    + " con los datos de usuario");
            return;
        }
        
        response.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter out = response.getWriter()) {
            out.print(toJson(productos));
        }
    }
    
    public static String toJson(Producto[] productos){
        String array = "[";
        for(int i=0; i<productos.length; i++){
                array += toJson(productos[i]);
                if(i<productos.length-1){
                    array += ",";
                }
            }
        array += "]";
        
        return array;
    }
    
    public static String toJson(Producto producto){
        return String.format(new Locale("en") ,"{"
                + "\"id\":%d,"
                + "\"nombre_producto\":\"%s\","
                + "\"marca\":\"%s\","
                + "\"unidad\":\"%s\","
                + "\"descripcion\":\"%s\","
                + "\"precio_dolares\":%.2f"
                + "}",
                producto.getId(),
                producto.getNombre(),
                producto.getMarca(),
                producto.getUnidad(),
                producto.getDescripcion(),
                producto.getPrecioDolar());
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
