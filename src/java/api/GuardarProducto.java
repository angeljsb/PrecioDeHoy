/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.ControlUsuario;
import backend.NoEncontradoException;
import backend.Producto;
import backend.RequestReader;
import backend.ResponseWriter;
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
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static javax.servlet.http.HttpServletResponse.SC_PAYMENT_REQUIRED;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Endpoint para guardar un producto en la base de datos. Guarda el producto
 * y lo devuelve como json con su id incluido
 *
 * @author Angel
 * @since v1.0.0
 */
@WebServlet(name = "GuardarProducto", urlPatterns = {"/api/guardarproducto"})
public class GuardarProducto extends HttpServlet {
    
    public static final int MAXIMO_PRODUCTOS = 30;

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
        RequestReader reader = new RequestReader(request);
        ResponseWriter writer = new ResponseWriter(response);
        
        if(user == null || user.getId() == 0){
            writer.sendError(SC_UNAUTHORIZED, "Debes estar logueado para realizar esta acción");
            return;
        }
        
        Producto ingresar = new Producto();
        ingresar.setUser(user.getId());
        ingresar.setNombre(reader.getString("nombre_producto"));
        ingresar.setMarca(reader.getString("marca"));
        ingresar.setUnidad(reader.getString("unidad"));
        ingresar.setDescripcion(reader.getString("descripcion"));
        ingresar.setPrecioDolar(reader.getDouble("precio"));
        
        if(ingresar.getNombre()==null||ingresar.getNombre().isEmpty()){
            writer.sendError(SC_BAD_REQUEST, "Nombre del producto es obligatorio");
            return;
        }
        
        TablaProducto tp = new TablaProducto();
        
        Producto ingresado;
        try{
            if(tp.countProductosUsuario(user.getId()) >= MAXIMO_PRODUCTOS){
                writer.sendError(SC_PAYMENT_REQUIRED, "Limite de productos exedido");
                return;
            }
            
            ingresado = tp.insert(ingresar);
        }catch(SQLException|NoEncontradoException ex){
            System.err.println(ex);
            writer.sendError(SC_BAD_REQUEST, ex.getMessage());
            return;
        }
        
        
//        System.out.println(new JSONObject(ingresado));
//        
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
