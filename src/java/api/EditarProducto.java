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
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Endpoint para editar un producto. Recibe por paramteros la información nueva
 * del producto y lo actualiza en la base de datos.<br>
 * Devuelve un json con los datos del producto.
 *
 * @author Angel
 * @since v1.0.0
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
     * @since v1.0.0
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        Usuario user = ControlUsuario.getUsuarioActual(request);
        RequestReader reader = new RequestReader(request);
        ResponseWriter writer = new ResponseWriter(response);
        
        if(user == null){
            writer.sendError(SC_UNAUTHORIZED, "Debes estar logueado para realizar esta acción");
            return;
        }
        
        Producto actualizar = new Producto();
        actualizar.setId(reader.getInt("producto_id"));
        actualizar.setUser(user.getId());
        actualizar.setNombre(reader.getString("nombre_producto"));
        actualizar.setMarca(reader.getString("marca"));
        actualizar.setUnidad(reader.getString("unidad"));
        actualizar.setDescripcion(reader.getString("descripcion"));
        actualizar.setPrecioDolar(reader.getDouble("precio"));
        
        if(actualizar.getNombre()==null||actualizar.getNombre().isEmpty()){
            writer.sendError(SC_BAD_REQUEST, "El nombre del producto es obligatorio");
            return;
        }
        
        Producto ingresado;
        try{
            TablaProducto tp = new TablaProducto();
            ingresado = tp.update(actualizar);
        }catch(SQLException|NoEncontradoException ex){
            writer.sendError(SC_BAD_REQUEST, ex.getMessage());
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
