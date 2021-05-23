/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 * Clase con variables y funciones estaticas para guardar y manejar los url
 * de los recursos que usa la aplicación
 *
 * @author Angel
 * @since v1.0.0
 */
public class AdministradorRecursos {
    
    public static final String UPLOADS = "/Upload";
    public static final String IMAGES_DIR = UPLOADS + "/img";
    
    public static final String HOME_PAGE = "/PrecioDeHoy";
    public static final String REGISTRO = HOME_PAGE + "/registro.jsp";
    public static final String INICIO_SECCION = HOME_PAGE +"/login.jsp";
    public static final String IMPRIMIR = HOME_PAGE + "/print.jsp";
    public static final String CERRAR_SECCION = HOME_PAGE + "/logout";
    
    public static final String API = HOME_PAGE + "/api";
    public static final String PRECIO_OFICIAL = API + "/precio";
    public static final String GUARDAR_PRODUCTO = API + "/guardarproducto";
    public static final String BORRAR_PRODUCTO = API + "/eliminarproducto";
    public static final String PRODUCTOS_USUARIO = API + "/productos";
    
    /**
     * Ejecuta una consulta GET o POST a un recurso externo y devuelve el
     * resultado como una cadena de texto
     * 
     * @param uri El url al que consultar
     * @param method El metodo de la consulta (De momento solo acepta GET y POST)
     * @param parametros Los parametros que se pasarán a la consulta.
     * ---- To do cambiar parametros por un JSONObject o un Map
     * @return La respuesta del servidor como un String
     * @since v1.0.0
     */
    public static String consultarApiForanea(String uri, String method, Parametro... parametros){
        
        Client cliente = ClientBuilder.newClient();
        WebTarget target = cliente.target(uri);
        
        String resultado = "";
        
        if(method.equalsIgnoreCase("GET")){
            resultado = target.request().get(String.class);
        }else if (method.equalsIgnoreCase("POST")){
            Form form = new Form();
            for(Parametro parametro : parametros){
                form.param(parametro.getKey(), parametro.getValue());
            }
        
            resultado = target.request().post(Entity.form(form) ,String.class);
        }
        
        cliente.close();
        
        return resultado;
    }
    
}
