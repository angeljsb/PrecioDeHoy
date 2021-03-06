/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.api;

import com.preciodehoy.preciodehoy.backend.NoEncontradoException;
import com.preciodehoy.preciodehoy.backend.ResponseWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import com.preciodehoy.preciodehoy.backend.TablaPrecio;
import com.preciodehoy.preciodehoy.beans.Proveedor;
import java.sql.SQLException;
import org.json.JSONArray;
import org.json.JSONObject;
import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

/**
 * Endpoint que devuelve los precios del dolar según los proveedores. Si
 * después de /api/precio/ se coloca el simbolo de un proveedor, devuelve
 * solo los datos de ese proveedor. Sino, devuelve un json array con todos
 *
 * @author Angel
 * @since v1.0.0
 */
@WebServlet(name = "PrecioOficial", urlPatterns = {"/api/precio/*"})
public class PrecioOficial extends HttpServlet {
    
    /**
     * Obtiene todos los proveedores con sus precios actuales desde la base de
     * datos
     * --- To do cambiar de lugar la función estatica ---
     * 
     * @return Un arreglo con todos los proveedores
     * @since v1.0.0
     */
    public static Proveedor[] getPrecios(){
        TablaPrecio tp = new TablaPrecio();
        Proveedor[] proveedores = tp.read();
        return proveedores;
    }
    
    /**
     * Obtiene el precio de un dolar en la base de datos según un proveedor
     * --- To do cambiar de lugar la función estatica ---
     * 
     * @param simbolo El simbolo único del proveedor
     * @return Un arreglo con todos los proveedores
     * @throws com.preciodehoy.preciodehoy.backend.NoEncontradoException Si el simbolo no existe
     * @since v1.0.0
     */
    public static Proveedor getPrecio(String simbolo) throws NoEncontradoException{
        TablaPrecio tp = new TablaPrecio();
        
        Proveedor proveedor = new Proveedor();
        
        try {
            proveedor = tp.readOne(simbolo);
        } catch (SQLException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        
        return proveedor;
    }

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
        
        //Cambio las especificaciones de la respuesta
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setCharacterEncoding("UTF-8");
        ResponseWriter writer = new ResponseWriter(response);
        
        //Obtengo los datos del url
        String simbolo = request.getPathInfo();
        
        //Instancio la clase que conecta con la tabla de la bd
        TablaPrecio tp = new TablaPrecio();
        
        //Cadena que será devuelta al final
        String proveedoresDevueltos;
        
        if(simbolo == null || simbolo.equals("/")){
            Proveedor[] proveedores = getPrecios();

            JSONArray array = new JSONArray(proveedores);
            proveedoresDevueltos = array.toString();
        }else{
            simbolo = simbolo.substring(1);
            Proveedor proveedor;
            try{
                proveedor = tp.readOne(simbolo);
            }catch(SQLException|NoEncontradoException ex){
                writer.sendError(SC_BAD_REQUEST, ex.getMessage());
                return;
            }
            proveedoresDevueltos = new JSONObject(proveedor).toString();
        }
        
        
        try (PrintWriter out = response.getWriter()) {
            out.print(proveedoresDevueltos);
        }
    }
    
    public static JSONObject proveedorToJson(Proveedor prov){
        JSONObject proveedor = new JSONObject();
        proveedor.put("nombre", prov.getNombreProveedor());
        proveedor.put("simbolo", prov.getSimbolo());
        proveedor.put("url", prov.getUrl());
        proveedor.put("colorAsociado", prov.getColor());
        
        JSONObject precio = new JSONObject();
        precio.put("texto", prov.getPrecioTexto());
        precio.put("numero", prov.getPrecio());
        precio.put("inverso", (prov.getPrecio()==0 ? 0 : 1/prov.getPrecio()));
        
        JSONObject buscador = new JSONObject();
        buscador.put("proveedor", proveedor);
        buscador.put("precio", precio);
        
        return buscador;
    }
    
    public static JSONArray proveedoresToJson(Proveedor[] proveedores){
        JSONArray array = new JSONArray();
        for(Proveedor proveedor : proveedores){
            array.put(proveedorToJson(proveedor));
        }
        return array;
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
        return "Devuelve información sobre el precio de una moneda en bolivares";
    }// </editor-fold>

}
