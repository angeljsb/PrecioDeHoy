/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.preciodehoy.preciodehoy.beans.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Arrays;

/**
 * Clase que controla las operaciones de guardado y lectura de los precios
 * del dolar en la base de datos
 *
 * @author Angel
 * @since v1.0.0
 */
public class TablaPrecio {
    
    private static final String NOMBRE_TABLA = "precio_proveedor";
    
    private static final String ID = "id",
            NOMBRE = "proveedor",
            SIMBOLO = "simbolo",
            URL = "sitio_web",
            COLOR = "color",
            PRECIO_D = "precio_numero",
            PRECIO_S = "precio_texto",
            FECHA = "ultima_modificacion";
    
    /**
     * Crea la tabla si no existe
     * 
     * @throws SQLException Si hay un error en la consulta sql
     * @since v1.0.0
     */
    public void crearTabla() throws SQLException{
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement ps = con.prepareStatement(this.crearTablaQuery());
        ps.execute();
    }
    
    /**
     * Crea un proveedor en la base de datos
     * 
     * @param insertar Los datos del nuevo proveedor
     * @return El nuevo proveedor luego de ser insertado con su id actualizado
     * @throws SQLException Si hay un error en la consulta sql
     * @since v1.0.0
     */
    public Proveedor create(Proveedor insertar) 
            throws SQLException{
        this.crearTabla();
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement insert = con.prepareStatement(
                "INSERT INTO " + NOMBRE_TABLA + " ("
                        + NOMBRE + ", "
                        + SIMBOLO + ", " + URL + ", " 
                        + COLOR + ", "
                        + PRECIO_D + ", " + PRECIO_S + ", "
                        + FECHA
                        + ") VALUES "
                        + "(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP())"
        );
        insert.setString(1, insertar.getNombreProveedor());
        insert.setString(2, insertar.getSimbolo());
        insert.setString(3, insertar.getUrl());
        insert.setInt(4, insertar.getColor());
        insert.setDouble(5, insertar.getPrecio());
        insert.setString(6, insertar.getPrecioTexto());
        
        insert.executeUpdate();
        
        PreparedStatement select = con.prepareStatement(
                "SELECT * FROM " + NOMBRE_TABLA
                        + " ORDER BY " + ID + " DESC LIMIT 1"
        );
        ResultSet resultado = select.executeQuery();
        resultado.first();
        
        return this.desdeResultSet(resultado);
    }
    
    /**
     * Lee todos los proveedores con sus últimos precios guardados desde la
     * base de datos
     * 
     * @return Un arreglo con todos los proveedores
     * @since v1.0.0
     */
    public Proveedor[] read(){
        try{
            this.crearTabla();
            Connection con = ControladorConexion.getConnection();

            PreparedStatement read = con.prepareStatement(this.readQuery());
            ResultSet resultado = read.executeQuery();

            return this.arrayDesdeResultSet(resultado);
        }catch(SQLException ex){
            return new Proveedor[0];
        }
    }
    
    /**
     * Lee el proveedor correspondiente al simbolo pasado.
     * 
     * @param simbolo El simbolo único del proveedor
     * @return El proveedor con sus datos actualizados
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si el símbolo no se encuentra en la base
     * de datos
     * @since v1.0.0
     */
    public Proveedor readOne(String simbolo) 
            throws SQLException, NoEncontradoException{
        this.crearTabla();
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement read = con.prepareStatement(
        "SELECT * FROM " + NOMBRE_TABLA + " WHERE " + SIMBOLO + " = ?");
        read.setString(1, simbolo);
        
        ResultSet resultado = read.executeQuery();
        if(!resultado.first()){
            throw new NoEncontradoException(SIMBOLO);
        }
        return this.desdeResultSet(resultado);
    }

    /**
     * Actualiza el precio según un proveedor en la base de datos, utilizando
     * la devolución de un buscador
     * 
     * @param buscador El buscador que obtenga el precio de la moneda según el
     * proveedor
     * @param id El id del proveedor a actualizar
     * @return Si el precio se atualizó correctamente
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si el id no se encuentra en la base de datos
     * @since v1.0.0
     */
    public boolean actualizarPrecio(BuscadorMoneda buscador, int id) throws SQLException, NoEncontradoException {        
        this.crearTabla();
        Connection con = ControladorConexion.getConnection();
        
        double precio = buscador.obtenerPrecio();
        String precioF = buscador.obtenerPrecioFormateado();
        
        if(precio == 0){
            System.err.println(buscador.getClass() + " falló al buscar el precio");
            return false;
        }
        
        PreparedStatement update = con.prepareStatement(
                "UPDATE " + NOMBRE_TABLA + " SET "
                        + PRECIO_D + " = ?, "
                        + PRECIO_S + " = ?, "
                        + FECHA + " = CURRENT_TIMESTAMP()"
                        + " WHERE " + ID + " = ? AND " + PRECIO_D + " != ?");
        update.setDouble(1, precio);
        update.setString(2, precioF);
        update.setInt(3, id);
        update.setDouble(4, precio);
        
        int rows = update.executeUpdate();
        
        return rows != 0;
    }

    /**
     * Borra un proveedor de la base de datos
     * 
     * @param idElemento El id del proveedor en la base de datso
     * @return Si se borró correctamente
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si no se encuentra el id
     * @since v1.0.0
     */
    public boolean delete(int idElemento) 
            throws SQLException, NoEncontradoException {
        this.crearTabla();
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement delete = con.prepareStatement(
                "DELETE FROM " + NOMBRE_TABLA + " WHERE " + ID + " = ?");
        delete.setInt(1, idElemento);
        
        int rows = delete.executeUpdate();
        
        if(rows==0){
            throw new NoEncontradoException(ID);
        }
        
        return true;
    }

    private Proveedor desdeResultSet(ResultSet rs) throws SQLException {
        Proveedor prov = new Proveedor();
        prov.setId(rs.getInt(ID));
        prov.setNombreProveedor(rs.getString(NOMBRE));
        prov.setSimbolo(rs.getString(SIMBOLO));
        prov.setUrl(rs.getString(URL));
        prov.setColor(rs.getInt(COLOR));
        prov.setPrecio(rs.getDouble(PRECIO_D));
        prov.setPrecioTexto(rs.getString(PRECIO_S));
        
        return prov;
    }

    private Proveedor[] arrayDesdeResultSet(ResultSet rs) throws SQLException{
        Proveedor[] arreglo = new Proveedor[0];
        rs.beforeFirst();
        int i=0;
        while(rs.next()){
            arreglo = Arrays.copyOf(arreglo, i+1);
            arreglo[i] = desdeResultSet(rs);
            i++;
        }
        return (Proveedor[]) arreglo;
    }
    
    protected String crearTablaQuery() {
        return "CREATE SEQUENCE IF NOT EXISTS id_precio;"
                + " CREATE TABLE IF NOT EXISTS " + NOMBRE_TABLA + " ("
                + ID + " INT DEFAULT NEXTVAL('id_precio') PRIMARY KEY,"
                + NOMBRE + " VARCHAR(50) NOT NULL,"
                + SIMBOLO + " VARCHAR(15) UNIQUE NOT NULL,"
                + URL + " VARCHAR(200) NOT NULL,"
                + COLOR + " INT NOT NULL,"
                + PRECIO_D + " DOUBLE,"
                + PRECIO_S + " VARCHAR(20),"
                + FECHA + " DATETIME );";
    }

    private String readQuery() {
        return "SELECT * FROM " + NOMBRE_TABLA + " ORDER BY " + ID;
    }
    
}
