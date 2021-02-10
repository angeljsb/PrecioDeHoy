/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import org.json.JSONObject;

/**
 *
 * @author Angel
 */
public class BuscadorLocalBitcoins extends BuscadorMoneda {
    
    public final static String URL_BASE = "https://localbitcoins.com/api/equation/USD_in_VES";
    
    @Override
    protected double obtenerDesdeWeb(){
        
        String respuesta = AdministradorRecursos.consultarApiForanea(URL_BASE, 
                "GET");
        
        JSONObject rJson = new JSONObject(respuesta);
        double localbtc =  rJson.getDouble("data");
        
        return localbtc;
    }
}
