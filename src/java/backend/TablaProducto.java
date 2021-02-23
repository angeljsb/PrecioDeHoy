/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.Arrays;

/**
 *
 * @author Angel
 */
public class TablaProducto {
    
    private static final String NOMBRE_TABLA = "producto";
    
    private static final String ID = "id",
            USUARIO = "user_id",
            NOMBRE = "nombre_producto",
            MARCA = "marca",
            UNIDAD = "unidad",
            DESCRIPCION = "descripcion",
            PRECIO = "precio_dolar",
            FECHA = "fecha_modificacion";
    
    public void crearTabla() throws SQLException{
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS "+ NOMBRE_TABLA +" ("
                        + ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + USUARIO + " INT NOT NULL,"
                        + NOMBRE + " VARCHAR(50) NOT NULL,"
                        + MARCA + " VARCHAR(50),"
                        + UNIDAD + " VARCHAR(50),"
                        + DESCRIPCION + " VARCHAR(250),"
                        + PRECIO + " DOUBLE UNSIGNED,"
                        + FECHA + " DATETIME NOT NULL,"
                        + " FOREIGN KEY (" + USUARIO + ") REFERENCES " 
                        + TablaUsuario.NOMBRE_TABLA + "(" + TablaUsuario.ID + ")"
                        + ") CHARACTER SET UTF8");
        preparedS.execute();
        
    }
    
    public Producto insert(int usuario, String nombre, String marca, 
            String unidad, String descripcion, double precioDolares, 
            int authCode) 
            throws SQLException, NoEncontradoException{
        
        crearTabla();
        
        TablaUsuario tu = new TablaUsuario();
        tu.autenticar(usuario, authCode);
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement insert = con.prepareStatement(
                "INSERT INTO " + NOMBRE_TABLA + " ("
                        + USUARIO + ", " + NOMBRE + ", "
                        + MARCA + ", " + UNIDAD + ", " 
                        + DESCRIPCION + ", "
                        + PRECIO + ", " + FECHA
                        + ") VALUES "
                        + "(?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP())"
        );
        insert.setInt(1, usuario);
        insert.setString(2, nombre);
        insert.setString(3, marca);
        insert.setString(4, unidad);
        insert.setString(5, descripcion);
        insert.setDouble(6, precioDolares);
        
        insert.executeUpdate();
        
        PreparedStatement select = con.prepareStatement(
                "SELECT P." + ID + ", P." + NOMBRE + ", P." + MARCA + ", P."
                        + UNIDAD + ", P."
                        + DESCRIPCION + ", P." + PRECIO
                        + " FROM " + NOMBRE_TABLA + " P, " 
                        + TablaUsuario.NOMBRE_TABLA + " U "
                        + "WHERE P." + USUARIO + " = ? AND U." 
                        + TablaUsuario.CODIGO_AUTENTICACION + " = ?"
                        + " ORDER BY P." + ID + " DESC LIMIT 1"
        );
        select.setInt(1, usuario);
        select.setInt(2, authCode);
        ResultSet resultado = select.executeQuery();
        resultado.first();
        
        return productoDesdeRS(resultado);
    }
    
    public Producto update(int usuario, String nombre, String marca, 
            String unidad, String descripcion, double precioDolares, 
            int authCode, int idProducto) 
            throws SQLException, NoEncontradoException{
        
        TablaUsuario tu = new TablaUsuario();
        tu.autenticar(usuario, authCode);
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement update = con.prepareStatement(
                "UPDATE " + NOMBRE_TABLA + " SET "
                        + NOMBRE + " = ?, "
                        + MARCA + " = ?, "
                        + UNIDAD + " = ?, "
                        + DESCRIPCION + " = ?, "
                        + PRECIO + " = ? "
                        + " WHERE " + ID + " = ?");
        update.setString(1, nombre);
        update.setString(2, marca);
        update.setString(3, unidad);
        update.setString(4, descripcion);
        update.setDouble(5, precioDolares);
        update.setInt(6, idProducto);
        
        update.executeUpdate();
        
        Producto producto = new Producto();
        producto.setId(idProducto);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setUnidad(unidad);
        producto.setDescripcion(descripcion);
        producto.setPrecioDolar(precioDolares);
        
        return producto;
    }
    
    public Producto[] getProductosUsuario(int userId, int authCode) throws SQLException{
        
        this.crearTabla();
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement select = con.prepareStatement(
                "SELECT P." + ID + ", P." + NOMBRE + ", P." + MARCA + ", P."
                        + UNIDAD + ", P."
                        + DESCRIPCION + ", P." + PRECIO
                        + " FROM " + NOMBRE_TABLA + " P, " 
                        + TablaUsuario.NOMBRE_TABLA + " U "
                        + "WHERE P." + USUARIO + " = ? AND U." 
                        + TablaUsuario.CODIGO_AUTENTICACION + " = ?"
                        + " ORDER BY " + FECHA + " ASC"
        );
        select.setInt(1, userId);
        select.setInt(2, authCode);
        
        ResultSet resultado = select.executeQuery();
        
        return arrayDesdeRS(resultado);
    }
    
    public int borrarProducto(int productoId, int userId, int authCode) throws NoEncontradoException, SQLException{
        
        Connection con = ControladorConexion.getConnection();
        
        TablaUsuario tu = new TablaUsuario();
        tu.autenticar(userId, authCode);
        
        PreparedStatement delete = con.prepareStatement(
                "DELETE FROM " + NOMBRE_TABLA + " WHERE " + ID + " = ? AND "
                        + USUARIO + " = ?"
        );
        delete.setInt(1, productoId);
        delete.setInt(2, userId);
        
        int rows = delete.executeUpdate();
        
        if(rows==0){
            throw new NoEncontradoException(ID);
        }
        
        return rows;
    }
    
    private Producto productoDesdeRS(ResultSet rs) throws SQLException{
        
        Producto devuelto = new Producto();
        devuelto.setId(rs.getInt(ID));
        devuelto.setNombre(rs.getString(NOMBRE));
        devuelto.setMarca(rs.getString(MARCA));
        devuelto.setUnidad(rs.getString(UNIDAD));
        devuelto.setDescripcion(rs.getString(DESCRIPCION));
        devuelto.setPrecioDolar(rs.getDouble(PRECIO));
        
        return devuelto;
    }
    
    private Producto[] arrayDesdeRS(ResultSet rs) throws SQLException{
        
        Producto[] productos = new Producto[0];
        
        rs.beforeFirst();
        
        int i = 0;
        while(rs.next()){
            productos = Arrays.copyOf(productos, i+1);
            productos[i] = productoDesdeRS(rs);
            i++;
        }
        
        return productos;
    }
    
}
