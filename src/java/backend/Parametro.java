/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 * Define un parametro llave/valor
 * --- To do borrar esta clase ---
 *
 * @author Angel
 */
public class Parametro {
    
    private String key;
    private String value;
    
    public Parametro(String key, Object value){
        this.key = key;
        this.value = value.toString();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
