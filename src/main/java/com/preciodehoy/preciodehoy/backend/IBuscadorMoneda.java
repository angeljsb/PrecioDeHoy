/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import java.text.DecimalFormat;

/**
 * Interfaz que representa a todas las clases encargadas de obtener el precio
 * de una moneda en relación con otra.
 * @author Angel
 * @since v1.0.0
 */
public interface IBuscadorMoneda {
    
    /**
     * Objeto para dar al precio el formato adecuado a la hora de
     * mostrarlo como texto
     * @since v1.0.0
     */
    public DecimalFormat FORMATO = new DecimalFormat("#,##0.00");
    
    /**
     * Obtiene el precio de la moneda especificada en formato <code>double</code>
     * @return El precio en bolivares de la moneda
     * @since v1.0.0
     */
    public double obtenerPrecio();
    
    /**
     * Obtiene el precio de la moneda como una cadena de texto en un
     * formato especifico, separando los decimales de las unidades con una
     * coma(,) y separando cada tres digitos por un punto(.)
     * Ejemplo: 20.000,05
     * @return El precio de la moneda como una cadena de texto
     * @since v1.0.0
     */
    public String obtenerPrecioFormateado();
    
}
