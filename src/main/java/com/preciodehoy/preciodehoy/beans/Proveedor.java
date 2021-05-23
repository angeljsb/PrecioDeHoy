/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.beans;

/**
 * Bean que define un proveedor. Se le llama proveedores a las casas de cambio
 * que proveen el precio del dolar
 *
 * @author Angel
 */
public class Proveedor {
    
    private int id;
    private String nombreProveedor;
    private String simbolo;
    private String url;
    private int Color;
    private double precio;
    private String precioTexto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String nombreProveedor) {
        this.nombreProveedor = nombreProveedor;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int Color) {
        this.Color = Color;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getPrecioTexto() {
        return precioTexto;
    }

    public void setPrecioTexto(String precioTexto) {
        this.precioTexto = precioTexto;
    }
    
}
