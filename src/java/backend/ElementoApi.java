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
@FunctionalInterface
public interface ElementoApi {
    
    /**
     * Convirte el elemento en un texto de formato json
     * @return Un texto de formato json con información sobre el
     * elemento.
     */
    public String toJson();
    
}
