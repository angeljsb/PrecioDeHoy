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
import org.json.JSONObject;

/**
 *
 * @author Angel
 */
public class RequestReader {
    
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
    
    public static JSONObject getParamsJson(InputStream is) throws IOException{
        String json = readInputStream(is);
        return new JSONObject(json);
    }
    
    private boolean json;
    private Map<String,String[]> mapa;
    private JSONObject jsonContent;
    
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
    
    public int getInt(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getInt(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Integer.parseInt(val);
        }
    }
    
    public double getDouble(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getDouble(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Double.parseDouble(val);
        }
    }
    
    public long getLong(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getLong(key) : 0;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? 0 : Long.parseLong(val);
        }
    }
    
    public boolean getBoolean(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getBoolean(key) : false;
        }else{
            String val = mapa.get(key)[0];
            return val == null ? false : Boolean.parseBoolean(val);
        }
    }
    
    public String getString(String key){
        if(this.isJson()){
            return this.jsonContent.has(key) ? this.jsonContent.getString(key) : "";
        }else{
            String val = mapa.get(key)[0];
            return val;
        }
    }

    public boolean isJson() {
        return this.json;
    }
    
}
