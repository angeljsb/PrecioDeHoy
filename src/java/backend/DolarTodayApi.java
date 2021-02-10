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
public class DolarTodayApi extends BuscadorDolarToday implements BuscadorMonedaApi {

    @Override
    public String getNombreProveedor() {
        return "DolarToday";
    }
    
    @Override
    public String getSimbolo(){
        return "DToday";
    }

    @Override
    public String getURLProveedor() {
        return "https://dolartoday.com/";
    }

    @Override
    public int getColorAsociado() {
        return 0x108F34;
    }
    
}
