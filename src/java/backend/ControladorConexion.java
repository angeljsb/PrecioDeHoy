/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Angel
 */
public class ControladorConexion {
    
    private final static String USERNAME = "root",
            PORT = "jdbc:mysql://localhost:3306/precio_de_hoy",
            PASSWORD = "hermanita";
    
    /**
     * Variable que guarda la conexion a la base de datos
     */
    private static Connection conexion = null;
    
    private static void establecerConexion(){
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = DriverManager.getConnection(PORT,USERNAME,PASSWORD);
        } catch (ClassNotFoundException|SQLException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        
    }
    
    public static Connection getConnection(){
        if(conexion==null){
            establecerConexion();
        }
        return conexion;
    }
    
}
