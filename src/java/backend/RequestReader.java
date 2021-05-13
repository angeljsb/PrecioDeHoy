/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Objeto para leer los parametros de una request independientemente de si
 * el contenido es un tipo aceptado por defecto o es de tipo application/json
 *
 * @author Angel
 * @since v1.0.0
 */
public class RequestReader {
    
    /**
     * Lee todo el texto de un input stream
     * 
     * @param is El input stream a leer
     * @return Todo el contenido del input stream como texto
     * @throws IOException Si un error de I/O ocurre
     * @since v1.0.0
     */
    public static String readInputStream(InputStream is) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String res = "";
        String line;
        while((line = br.readLine()) != null){
            res += line;
        }
        System.out.println(res);
        return res;
    }
    
    /**
     * Lee un json de un input stream
     * 
     * @param is El input stream a leer
     * @return El contenido del input stream como un JSONOject
     * @throws IOException Si un error de I/O ocurre
     * @throws JSONException Si el contenido del input stream no es un JSON
     * @since v1.0.0
     */
    public static JSONObject getParamsJson(InputStream is) throws IOException, JSONException{
        String json = readInputStream(is);
        return new JSONObject(json);
    }
    
    private boolean json;
    private Map<String,String[]> mapa;
    private JSONObject jsonContent;
    
    /**
     * Crea un objeto para obtener los parametros de una request
     * 
     * @param request La request cuyos parametros se quieren leer
     * @since v1.0.0
     */
    public RequestReader(HttpServletRequest request){
        
        this.json = request.getContentType().startsWith(MediaType.APPLICATION_JSON);
        this.mapa = request.getParameterMap();
        
        if(this.json){
            try{
                this.jsonContent = getParamsJson(request.getInputStream());
            }catch(IOException ex){
                System.err.println(ex);
            }
        }
        
    }
    
    /**
     * Obtiene un parametro converitdo a int
     * 
     * @param key El nombre del parametro
     * @return El valor del parametro o cero si no lo encuentra
     * @since v1.0.0
     */
    public int getInt(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getInt(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Integer.parseInt(val);
        }
    }
    
    /**
     * Obtiene un parametro converitdo a double
     * 
     * @param key El nombre del parametro
     * @return El valor del parametro o cero si no lo encuentra
     * @since v1.0.0
     */
    public double getDouble(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getDouble(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Double.parseDouble(val);
        }
    }
    
    /**
     * Obtiene un parametro converitdo a long
     * 
     * @param key El nombre del parametro
     * @return El valor del parametro o cero si no lo encuentra
     * @since v1.0.0
     */
    public long getLong(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getLong(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Long.parseLong(val);
        }
    }
    
    /**
     * Obtiene un parametro converitdo a boolean
     * 
     * @param key El nombre del parametro
     * @return El valor del parametro o false si no lo encuentra
     * @since v1.0.0
     */
    public boolean getBoolean(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getBoolean(key) : false;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? false : Boolean.parseBoolean(val);
        }
    }
    
    /**
     * Obtiene un parametro converitdo a String
     * 
     * @param key El nombre del parametro
     * @return El valor del parametro o un String vacio si no lo encuentra
     * @since v1.0.0
     */
    public String getString(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getString(key) : "";
        }else{
            String val = mapa.get(key)[0];
            return val == null ? "" : val;
        }
    }

    /**
     * Dice si el contenido de la request es de tipo application/json
     * @return Si el contenido de la request leida es application/json
     * @since v1.0.0
     */
    public boolean isJson() {
        return this.json;
    }
    
}
