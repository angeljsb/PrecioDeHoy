/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import com.preciodehoy.preciodehoy.beans.Proveedor;
import org.json.JSONObject;

/**
 * Clase encargada de obtener el precio actual del dolar según LocalBitcoins
 *
 * @author Angel
 * @since v1.0.0
 */
public class BuscadorLocalBitcoins extends BuscadorMoneda {
    
    /**
     * URL de la api desde la cual obtnemos este precio
     */
    public final static String URL_BASE = "https://localbitcoins.com/api/equation/USD_in_VES";
    
    @Override
    protected double obtenerDesdeWeb(){
        
        String respuesta = AdministradorRecursos.consultarApiForanea(URL_BASE, 
                "GET");
        
        JSONObject rJson = new JSONObject(respuesta);
        double localbtc =  rJson.getDouble("data");
        
        return localbtc;
    }

    @Override
    public Proveedor getProveedor() {
        Proveedor prov = new Proveedor();
        prov.setId(3);
        prov.setNombreProveedor("LocalBitcoins");
        prov.setSimbolo("LocalBTC");
        prov.setUrl("https://localbitcoins.com/");
        prov.setColor(0xf58220);
        prov.setPrecio(this.obtenerPrecio());
        prov.setPrecioTexto(this.obtenerPrecioFormateado());
        return prov;
    }
}
