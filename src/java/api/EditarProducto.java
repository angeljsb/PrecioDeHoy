/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.NoEncontradoException;
import backend.Producto;
import backend.TablaProducto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

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
        
        int userId,
                authCode,
                productoId;
        String nombre,
                marca,
                unidad,
                descripcion;
        double precioDolar;
        
        if(request.getContentType().startsWith(MediaType.APPLICATION_JSON)){
            BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
            String req = br.readLine();
            JSONObject content = new JSONObject(req);
            
            userId = content.getInt("user_id");
            authCode = content.getInt("auth_code");
            productoId = content.getInt("producto_id");
            nombre = content.getString("nombre_producto");
            marca = content.getString("marca");
            unidad = content.getString("unidad");
            descripcion = content.getString("descripcion");
            precioDolar = content.getDouble("precio");
            
        }else{
        
        
            String userIdP = request.getParameter("user_id"), 
                    precioDolaresP = request.getParameter("precio"),
                    authCodeStr = request.getParameter("auth_code"),
                    productoIdStr = request.getParameter("producto_id");
            nombre = request.getParameter("nombre_producto");
            marca = request.getParameter("marca");
            unidad = request.getParameter("unidad");
            descripcion = request.getParameter("descripcion");
            
            if(userIdP==null||nombre==null||authCodeStr==null
                    ||userIdP.isEmpty()||nombre.isEmpty()||authCodeStr.isEmpty()
                    ||productoIdStr==null||productoIdStr.isEmpty()){
                response.sendError(400);
                return;
            }
            userId = Integer.parseInt(userIdP);
            authCode = Integer.parseInt(authCodeStr);
            productoId = Integer.parseInt(productoIdStr);
            precioDolar = precioDolaresP.isEmpty() 
                    ? 0 
                    : Double.parseDouble(precioDolaresP);
        }
        
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
