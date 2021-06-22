/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

//import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase que genera y controla la conexión a la base de datos durante toda
 * la ejecución del programa
 *
 * @author Angel
 * @since v1.0.0
 */
public class ControladorConexion {
    
    private final static String USERNAME = "root",
            PORT = "jdbc:mysql://localhost:3306/precio_de_hoy",
            PASSWORD = "hermanita";
    
    /**
     * Variable que guarda la conexion a la base de datos
     * @since v1.0.0
     */
    private static Connection conexion = null;
    
    /**
     * --- Función proveida por la documentación de heroku ---
     * @return
     * @throws URISyntaxException
     * @throws SQLException 
     */
    private static Connection getHerokuConnection() throws URISyntaxException, SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        System.out.println(dbUrl);
        return DriverManager.getConnection(dbUrl);
    }
    
    /**
     * Establece la conexión con la base de datos
     * @since v1.0.0
     */
    private static void establecerConexion(){
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conexion = /*getHerokuConnection(); */DriverManager.getConnection(PORT,USERNAME,PASSWORD);
        } catch (ClassNotFoundException|SQLException/*|URISyntaxException*/ ex) {
            System.err.println(ex);
        }
        
    }
    
    /**
     * Devuelve la conexión con la base de datos.<br>
     * De no haber sido establecida aún, pide establecerla antes de devolverla
     * 
     * @return La conexión con la bas de datos
     * @since v1.0.0
     */
    public static Connection getConnection(){
        if(conexion==null){
            establecerConexion();
        }
        return conexion;
    }
    
}
