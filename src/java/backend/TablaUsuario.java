/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Angel
 */
public class TablaUsuario {
    
    public static final String NOMBRE_TABLA = "usuario";
    
    public static final String ID = "id",
            NOMBRE = "nombre",
            CORREO = "correo",
            PASSWORD = "contraseña",
            CODIGO_AUTENTICACION = "auth_code";
    
    public void crearTabla() throws SQLException{
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS "+ NOMBRE_TABLA +" ("
                        + ID + " INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                        + NOMBRE + " VARCHAR(50),"
                        + CORREO + " VARCHAR(200) NOT NULL UNIQUE,"
                        + PASSWORD + " VARCHAR(50) NOT NULL,"
                        + CODIGO_AUTENTICACION + " INT NULL"
                        + ")");
        preparedS.execute();
        
    }
    
    private String camposBusqueda(){
        return String.format("%s, %s, %s, %s", ID, 
                NOMBRE, CORREO, CODIGO_AUTENTICACION );
    }
    
    public int insert(String nombre, String correo, String password) throws SQLException{
        
        crearTabla();
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "INSERT INTO " + NOMBRE_TABLA + " ("
                        + NOMBRE + ", " + CORREO + ", " + PASSWORD + ", "
                                + CODIGO_AUTENTICACION
                        + ") VALUES "
                        + "(?,?,SHA1(?),FLOOR(RAND()*(99999-10000)+10000))"
        );            
        preparedS.setString(1, nombre);
        preparedS.setString(2, correo);
        preparedS.setString(3, password);

        return preparedS.executeUpdate();
    }
    
    public Usuario obtenerUno(String campo, Object value) throws SQLException, NoEncontradoException{
        
        
        Connection con = ControladorConexion.getConnection();
        PreparedStatement preparedS = con.prepareStatement(
                "SELECT " + camposBusqueda() + " FROM " 
                        + NOMBRE_TABLA
                        + " WHERE " + campo + " = ?"
                        + " LIMIT 1"
        );
        if(value instanceof String){
            String string = (String) value;
            preparedS.setString(1, string);
        }else if(value instanceof Integer){
            Integer integer = (Integer) value;
            preparedS.setInt(1, integer);
        }


        ResultSet respuesta = preparedS.executeQuery();

        if(!respuesta.first()){
            throw new NoEncontradoException(campo);
        }

        return fromResultSet(respuesta);
            
        
    }
    
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
                        + " WHERE " + ID + " = ? AND " + PASSWORD + " = SHA1(?)"
        );
        peticionUsuario.setInt(1, idUser);
        peticionUsuario.setString(2, password);
        ResultSet userRS = peticionUsuario.executeQuery();
        
        if(!userRS.first()){
            throw new NoEncontradoException(PASSWORD);
        }
        
        return fromResultSet(userRS);
    }
    
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
        usuario.setId(rs.getInt(ID));
        usuario.setAuthCode(rs.getInt(CODIGO_AUTENTICACION));
        
        return usuario;
    }
    
}
