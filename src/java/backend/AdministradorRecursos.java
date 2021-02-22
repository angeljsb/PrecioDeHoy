/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;

/**
 *
 * @author Angel
 */
public class AdministradorRecursos {
    
    public static final String UPLOADS = "/Upload";
    public static final String IMAGES_DIR = UPLOADS + "/img";
    
    public static final String HOME_PAGE = "/PrecioDolar";
    public static final String REGISTRO = HOME_PAGE + "/registro.jsp";
    public static final String INICIO_SECCION = HOME_PAGE +"/login.jsp";
    public static final String CERRAR_SECCION = HOME_PAGE + "/logout";
    
    public static final String API = HOME_PAGE + "/api";
    public static final String PRECIO_OFICIAL = API + "/precio";
    public static final String GUARDAR_PRODUCTO = API + "/guardarproducto";
    public static final String BORRAR_PRODUCTO = API + "/eliminarproducto";
    public static final String PRODUCTOS_USUARIO = API + "/productos";
    
    public static String getAbsoluteUri(String uri){
        return "http://localhost:8080" + uri;
    }
    
    /**
     * Hace una consulta a la api local
     * @param uri Una de las constantes que guardan los url's en esta misma clase
     * @param parametros
     * @return El resultado de la consulta como un string
     */
    public static String consultarApiLocal(String uri, Parametro... parametros){
        
        Client cliente = ClientBuilder.newClient();
        WebTarget target = cliente.target(getAbsoluteUri(uri));
        
        Form form = new Form();
        for(Parametro parametro : parametros){
            form.param(parametro.getKey(), parametro.getValue());
        }
        
        String resultado = target.request().post(Entity.form(form) ,String.class);
        
        cliente.close();
        
        return resultado;
    }
    
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
