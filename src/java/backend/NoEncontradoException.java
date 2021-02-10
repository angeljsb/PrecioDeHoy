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
public class NoEncontradoException extends Exception {
    
    private String campoError;

    public NoEncontradoException(String campoError) {
        super("Ningún elemento en la base de datos coincidió"
                + " con el valor agregado en " + campoError);
        this.campoError = campoError;
    }
    
    public String getCampoError(){
        return campoError;
    }
    
}
