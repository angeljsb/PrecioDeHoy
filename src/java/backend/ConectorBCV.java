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
public class ConectorBCV extends ScrapperDolarOficial implements BuscadorMonedaApi {
    
    @Override
    public String getNombreProveedor() {
        return "Banco Central de Venezuela";
    }
    
    @Override
    public String getSimbolo(){
        return "BCV";
    }

    @Override
    public String getURLProveedor() {
        return BCV;
    }
    
    @Override
    public int getColorAsociado(){
        return 0x252F5D;
    }
}
