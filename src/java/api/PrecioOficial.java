/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package api;

import backend.BuscadorLocalBitcoinsApi;
import backend.ConectorBCV;
import backend.DolarTodayApi;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import backend.ElementoApi;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Angel
 */
@WebServlet(name = "PrecioOficial", urlPatterns = {"/api/precio"})
public class PrecioOficial extends HttpServlet {

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
        
        response.setContentType(MediaType.APPLICATION_JSON);
        ElementoApi[] buscadores = new ElementoApi[]{
            new ConectorBCV(),
            new DolarTodayApi(),
            new BuscadorLocalBitcoinsApi()
        };
        JSONArray array = new JSONArray();
        for(ElementoApi buscador : buscadores){
            array.put(new JSONObject(buscador.toJson()));
        }
        try (PrintWriter out = response.getWriter()) {
            out.print(array);
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
        return "Devuelve información sobre el precio de una moneda en bolivares";
    }// </editor-fold>

}
