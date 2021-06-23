/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase que controla la lectura y actualización de usuarios en la base
 * de datos
 *
 * @author Angel
 * @since v1.0.0
 */
public class TablaUsuario {
    
    public static final String NOMBRE_TABLA = "usuario";
    
    public static final String ID = "id",
            NOMBRE = "nombre_negocio",
            CORREO = "correo",
            PASSWORD = "contraseña",
            IMAGEN = "ruta_imagen",
            CODIGO_AUTENTICACION = "auth_code";
    
    /**
     * Crea la tabla si no existe
     * 
     * @throws SQLException Si ocurre un error en la consulta sql
     * @since v1.0.0
     */
    public void crearTabla() throws SQLException{
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "CREATE SEQUENCE IF NOT EXISTS id_usuario;"
                        + " CREATE TABLE IF NOT EXISTS "+ NOMBRE_TABLA +" ("
                        + ID + " INT DEFAULT NEXTVAL('id_usuario') PRIMARY KEY,"
                        + NOMBRE + " VARCHAR(50),"
                        + CORREO + " VARCHAR(200) NOT NULL UNIQUE,"
                        + PASSWORD + " VARCHAR(50) NOT NULL,"
                        + IMAGEN + " VARCHAR(250),"
                        + CODIGO_AUTENTICACION + " INT NULL"
                        + ");");
        preparedS.execute();
        
    }
    
    private String camposBusqueda(){
        return String.format("%s, %s, %s, %s, %s", ID, 
                NOMBRE, CORREO, IMAGEN , CODIGO_AUTENTICACION );
    }
    
    /**
     * Crea un usuario con los datos básicos de registro
     * 
     * @param nombre El nombre del usuario
     * @param correo El correo del usuario
     * @param password La contraseña del usuario
     * @param imagen El logo del negocio del usuario
     * @return La cantidad de columnas afectadas en la tabla
     * @throws SQLException Si ocurre un error en la consulta sql
     * @since v1.0.0
     */
    public int insert(String nombre, String correo, String password, String imagen) 
            throws SQLException{
        
        crearTabla();
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "INSERT INTO " + NOMBRE_TABLA + " ("
                        + NOMBRE + ", " + CORREO + ", " + PASSWORD + ", "
                        + IMAGEN + ", " + CODIGO_AUTENTICACION
                        + ") VALUES "
                        + "(?, ?, MD5(?), ?, FLOOR(RAND()*(99999-10000)+10000))"
        );            
        preparedS.setString(1, nombre);
        preparedS.setString(2, correo);
        preparedS.setString(3, password);
        preparedS.setString(4, imagen);

        return preparedS.executeUpdate();
    }
    
    /**
     * Obtiene el usuario con los datos comunes de inicio de sesión
     * 
     * @param correo El correo del usuario
     * @param password La contraseña del usuario
     * @return El usuario correspondiente a los datos
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si el correo no existe o la contraseña no
     * coincide
     * @since v1.0.0
     */
    public Usuario iniciarSeccion(String correo, String password) throws SQLException, NoEncontradoException{
        
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement peticionId = con.prepareStatement(
                "SELECT " + ID + " FROM " + NOMBRE_TABLA + " WHERE " + CORREO + " = ?"
        );
        peticionId.setString(1, correo);
        ResultSet idRS = peticionId.executeQuery();
        
        if(!idRS.first()){
            throw new NoEncontradoException(CORREO);
        }
        int idUser = idRS.getInt(ID);
        
        PreparedStatement peticionUsuario = con.prepareStatement(
                "SELECT " + camposBusqueda() + " FROM "
                        + NOMBRE_TABLA
                        + " WHERE " + ID + " = ? AND " + PASSWORD + " = "
                        + "MD5(?)"
        );
        peticionUsuario.setInt(1, idUser);
        peticionUsuario.setString(2, password);
        ResultSet userRS = peticionUsuario.executeQuery();
        
        if(!userRS.first()){
            throw new NoEncontradoException(PASSWORD);
        }
        
        return fromResultSet(userRS);
    }
    
    /**
     * Obtiene un usuario según los datos de autenticación guardados en cokkies
     * 
     * @param id El id del usuario
     * @param authCode El codigo de autenticación
     * @return El usuario correspondiente
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si el id no se encuentra o el codigo de
     * autenticació no coincide
     * @since v1.0.0
     */
    public Usuario autenticar(int id, int authCode) throws SQLException, NoEncontradoException{
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement peticionId = con.prepareStatement(
                "SELECT " + camposBusqueda() + " FROM " 
                        + NOMBRE_TABLA + " WHERE " + ID + " = ?"
        );
        peticionId.setInt(1, id);
        ResultSet userRS = peticionId.executeQuery();
        
        if(!userRS.first()){
            throw new NoEncontradoException(ID);
        }
        int codeUser = userRS.getInt(CODIGO_AUTENTICACION);
        
        if(codeUser!=authCode){
            throw new NoEncontradoException(CODIGO_AUTENTICACION);
        }
        
        return fromResultSet(userRS);
    }
    
    public boolean exists(int id) throws SQLException{
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement peticionId = con.prepareStatement(
                "SELECT " + camposBusqueda() + " FROM " 
                        + NOMBRE_TABLA + " WHERE " + ID + " = ?"
        );
        peticionId.setInt(1, id);
        ResultSet userRS = peticionId.executeQuery();
        
        return userRS.first();
    }
    
    /**
     * Ejecuta el cierre de sesión e impide el inicio automatico por cokkies
     * cambiando el codigo de autenticación en la base de datos
     * 
     * @param id El id del usuario a cerrar sesión
     * @throws SQLException Si ocurre un error en la consulta sql
     * @throws NoEncontradoException Si no se consigue el usuario
     * @since v1.0.0 
     */
    public void cerrarSeccion(int id) throws SQLException, NoEncontradoException{
        Connection con = ControladorConexion.getConnection();
        
        PreparedStatement cerrar = con.prepareStatement(
                "UPDATE " + NOMBRE_TABLA + " SET "
                        + CODIGO_AUTENTICACION + " = "
                        + "FLOOR(RAND()*(99999-10000)+10000)"
                        + " WHERE " + ID + " = ?"
        );
        cerrar.setInt(1, id);
        
        int rows = cerrar.executeUpdate();
        
        if(rows==0){
            throw new NoEncontradoException(ID);
        }
    }
    
    private Usuario fromResultSet(ResultSet rs) throws SQLException{
        rs.first();
        
        Usuario usuario = new Usuario();
        usuario.setNombre(rs.getString(NOMBRE));
        usuario.setCorreo(rs.getString(CORREO));
        usuario.setImagen(rs.getString(IMAGEN));
        usuario.setId(rs.getInt(ID));
        usuario.setAuthCode(rs.getInt(CODIGO_AUTENTICACION));
        
        return usuario;
    }
    
}
