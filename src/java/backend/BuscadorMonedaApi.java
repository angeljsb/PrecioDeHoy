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
public interface BuscadorMonedaApi extends IBuscadorMoneda, ElementoApi {
    
    public abstract String getNombreProveedor();
    
    public abstract String getURLProveedor();
    
    public abstract String getSimbolo();
    
    public abstract int getColorAsociado();
    
    @Override
    public default String toJson(){
        JSONObject proveedor = new JSONObject();
        proveedor.put("nombre", getNombreProveedor());
        proveedor.put("simbolo", getSimbolo());
        proveedor.put("url", getURLProveedor());
        proveedor.put("colorAsociado", getColorAsociado());
        
        JSONObject precio = new JSONObject();
        precio.put("texto", obtenerPrecioFormateado());
        precio.put("numero", obtenerPrecio());
        precio.put("inverso", (obtenerPrecio()==0 ? 0 : 1/obtenerPrecio()));
        
        JSONObject buscador = new JSONObject();
        buscador.put("proveedor", proveedor);
        buscador.put("precio", precio);
        
        return buscador.toString();
    }
    
}
