/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import com.preciodehoy.preciodehoy.beans.Proveedor;
import org.json.JSONObject;

/**
 * Clase encargada de obtener el precio actual del dolar según DolarToday
 *
 * @author Angel
 * @since v1.0.0
 */
public class BuscadorDolarToday extends BuscadorMoneda {
    
    /**
     * Url de la api desde la cual obtenemos este precio
     */
    public static final String URL = "https://s3.amazonaws.com/dolartoday/data.json"; 

    private final String[] rutaJSON = new String[]{"USD", "dolartoday"};
    
    @Override
    public double obtenerDesdeWeb(){
        
        String resultado = AdministradorRecursos.consultarApiForanea(URL, "GET");
        
        JSONObject respuesta = new JSONObject(resultado);
        
        int i;
        for(i=0; i<rutaJSON.length-1;i++){
            respuesta = respuesta.getJSONObject(rutaJSON[i]);
        }
        
        return respuesta.getDouble(rutaJSON[i]);
        
    }
    
    @Override
    public Proveedor getProveedor() {
        Proveedor prov = new Proveedor();
        prov.setId(2);
        prov.setNombreProveedor("DolarToday");
        prov.setSimbolo("DToday");
        prov.setUrl("https://dolartoday.com/");
        prov.setColor(0x108f34);
        prov.setPrecio(this.obtenerPrecio());
        prov.setPrecioTexto(this.obtenerPrecioFormateado());
        return prov;
    }
}
