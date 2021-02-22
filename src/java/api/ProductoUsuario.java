/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.Producto;
import backend.TablaProducto;
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

/**
 *
 * @author Angel
 */
@WebServlet(name = "ProductoUsuario", urlPatterns = {"/api/productos"})
public class ProductoUsuario extends HttpServlet {

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
        
        String userId = request.getParameter("user_id");
        String authCode = request.getParameter("auth_code");
        
        if(userId==null||authCode==null){
            response.sendError(400);
            return;
        }
        
        Producto[] productos;
        try {
            TablaProducto tp = new TablaProducto();
            productos = tp.getProductosUsuario(
                    Integer.parseInt(userId),
                    Integer.parseInt(authCode)
            );
        } catch (SQLException | NumberFormatException ex) {
            response.sendError(400);
            return;
        }
        
        response.setContentType(MediaType.APPLICATION_JSON);
        try (PrintWriter out = response.getWriter()) {

            out.print("[");
            for(int i=0; i<productos.length; i++){
                out.print(toJson(productos[i]));
                if(i<productos.length-1){
                    out.print(",");
                }
            }
            out.print("]");
        }
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
