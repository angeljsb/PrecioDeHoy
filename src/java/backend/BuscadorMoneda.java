/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 *
 * @author Angel
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
     */
    protected abstract double obtenerDesdeWeb();
    
}
