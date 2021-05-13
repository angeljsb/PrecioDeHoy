/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 * Representa un error al buscar un valor en la base de datos y no encontrarlo
 * o no coincidir con el valor esperado
 *
 * @author Angel
 * @since v1.0.0
 */
public class NoEncontradoException extends Exception {
    
    private String campoError;

    /**
     * Crea un error cuyo mensaje especifica cuál campo fue el que no
     * se encontró o no coincidió
     * 
     * @param campoError E  campo que no se encontró o no coincidió
     * @since v1.0.0
     */
    public NoEncontradoException(String campoError) {
        super("Ningún elemento en la base de datos coincidió"
                + " con el valor agregado en " + campoError);
        this.campoError = campoError;
    }
    
    /**
     * Obtiene el campo que no se encontró o no coincidió para este error
     * 
     * @return El campo que no se encontró o no coincidió
     * @since v1.0.0
     */
    public String getCampoError(){
        return campoError;
    }
    
}
