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
 * Clase que controla la lectura y actualización de los productos en la base de
 * datos
 *
 * @author Angel
 * @since v1.0.0
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
    
    /**
     * Crea la tabla de productos si no existe
     * 
     * @throws SQLException Si ocurre un error en la consulta sql
     * @since v1.0.0
     */
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
    
    /**
     * Crea un producto en la base de datos
     * --- To do usar bean en lugar de los parametros actuales ---
     * 
     * @param producto El producto a ingresar
     * @return El producto con su id actualizado
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si el usuario no se encuentra o si
     * el codigo de autenticación no corresponde
     * @since v1.0.0
     */
    public Producto insert(Producto producto) 
            throws SQLException, NoEncontradoException{
        
        crearTabla();
        
        int usuario = producto.getUser();
        String nombre = producto.getNombre();
        String marca = producto.getMarca();
        String unidad = producto.getUnidad();
        String descripcion = producto.getDescripcion();
        double precioDolares = producto.getPrecioDolar();
        
        TablaUsuario tu = new TablaUsuario();
        if (!tu.exists(usuario)){
            throw new NoEncontradoException(USUARIO);
        }
        
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
                        + "WHERE P." + USUARIO + " = ? " 
                        + "ORDER BY P." + ID + " DESC LIMIT 1"
        );
        select.setInt(1, usuario);
        ResultSet resultado = select.executeQuery();
        resultado.first();
        
        return productoDesdeRS(resultado);
    }
    
    /**
     * Actualiza un producto
     * --- To do usar bean en lugar de todos estos parametros ---
     * 
     * @param producto El producto a editar
     * @return El producto actualizado
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si no se encuentra el id del producto o
     * del usuario o si el codigo de autenticación no corresponde
     * @since v1.0.0
     */
    public Producto update(Producto producto) 
            throws SQLException, NoEncontradoException{
        
        int idProducto = producto.getId();
        int usuario = producto.getUser();
        String nombre = producto.getNombre();
        String marca = producto.getMarca();
        String unidad = producto.getUnidad();
        String descripcion = producto.getDescripcion();
        double precioDolares = producto.getPrecioDolar();
        
        TablaUsuario tu = new TablaUsuario();
        if(!tu.exists(usuario)){
            throw new NoEncontradoException(USUARIO);
        }
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement update = con.prepareStatement(
                "UPDATE " + NOMBRE_TABLA + " SET "
                        + NOMBRE + " = ?, "
                        + MARCA + " = ?, "
                        + UNIDAD + " = ?, "
                        + DESCRIPCION + " = ?, "
                        + PRECIO + " = ?, "
                        + FECHA + " = CURRENT_TIMESTAMP()"
                        + " WHERE " + ID + " = ?");
        update.setString(1, nombre);
        update.setString(2, marca);
        update.setString(3, unidad);
        update.setString(4, descripcion);
        update.setDouble(5, precioDolares);
        update.setInt(6, idProducto);
        
        update.executeUpdate();
        
        return producto;
    }
    
    /**
     * Obtiene todos los productos de un usuario
     * 
     * @param userId El id del usuario
     * @return Un arreglo con todos sus productos
     * @throws SQLException Si ocurre un error en la consulta sql
     * @since v1.0.0
     */
    public Producto[] getProductosUsuario(int userId) throws SQLException{
        
        this.crearTabla();
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement select = con.prepareStatement(
                "SELECT P." + ID + ", P." + NOMBRE + ", P." + MARCA + ", P."
                        + UNIDAD + ", P."
                        + DESCRIPCION + ", P." + PRECIO
                        + " FROM " + NOMBRE_TABLA + " P, " 
                        + TablaUsuario.NOMBRE_TABLA + " U "
                        + "WHERE P." + USUARIO + " = ?"
                        + " ORDER BY " + FECHA + " DESC"
        );
        select.setInt(1, userId);
        
        ResultSet resultado = select.executeQuery();
        
        return arrayDesdeRS(resultado);
    }
    
    /**
     * Borra un producto de la base de datos
     * 
     * @param productoId El id del producto
     * @param userId El id del usuario
     * @return La cantidad de lineas afectadas en la base de datos
     * @throws NoEncontradoException Si no se encuentra el producto o el usuario
     * o si el codigo de autenticación no corresponde
     * @throws SQLException  Si ocurre un error en la consulta sql
     * @since v1.0.0
     */
    public int borrarProducto(int productoId, int userId) throws NoEncontradoException, SQLException{
        
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement delete = con.prepareStatement(
                "DELETE FROM " + NOMBRE_TABLA + " WHERE " + ID + " = ? AND "
                        + USUARIO + " = ?"
        );
        delete.setInt(1, productoId);
        delete.setInt(2, userId);
        
        int rows = delete.executeUpdate();
        
        if(rows==0){
            TablaUsuario tu = new TablaUsuario();
            if(!tu.exists(userId)){
                throw new NoEncontradoException(USUARIO);
            }
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
