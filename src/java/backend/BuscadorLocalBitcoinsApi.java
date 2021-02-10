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
public class BuscadorLocalBitcoinsApi extends BuscadorLocalBitcoins implements BuscadorMonedaApi {

    @Override
    public String getNombreProveedor() {
        return "LocalBitcoins";
    }

    @Override
    public String getURLProveedor() {
        return "https://localbitcoins.com/";
    }

    @Override
    public String getSimbolo() {
        return "LocalBTC";
    }

    @Override
    public int getColorAsociado() {
        return 0xf58220;
    }
    
}
