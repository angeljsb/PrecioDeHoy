/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

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
                System.err.println(ex.getLocalizedMessage());
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
    
}
