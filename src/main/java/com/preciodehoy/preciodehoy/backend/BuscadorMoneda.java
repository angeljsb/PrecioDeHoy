/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import com.preciodehoy.preciodehoy.beans.Proveedor;

/**
 * Clase abstracta que define los objetos encargados de obtener el precio
 * del dolar desde un servicio o casa de cambio foraneo
 *
 * @author Angel
 * @since v1.0.0
 */
public abstract class BuscadorMoneda implements IBuscadorMoneda {
    
    private double precio = 0;

    @Override
    public String obtenerPrecioFormateado(){
        return FORMATO.format(obtenerPrecio());
    }

    @Override
    public double obtenerPrecio(){
        if(precio==0){
            try{
                precio = obtenerDesdeWeb();
            }catch(Exception ex){
                ex.printStackTrace(System.err);
            }
        }
        return precio;
    }
    
    /**
     * Se encarga de hacer las llamadas a la api del proveedor
     * @return El precio de la moneda según el proveedor
     * @since v1.0.0
     */
    protected abstract double obtenerDesdeWeb();
    
    /**
     * Devuelve el proveedor base para este buscador
     * 
     * @return El objeto (bean) con los datos de este proveedor
     */
    public abstract Proveedor getProveedor();
    
}
