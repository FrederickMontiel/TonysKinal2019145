package org.frederickmontiel.db;

import com.mysql.jdbc.Driver;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
 
    private Connection conexion;
    private static Conexion Instancia;
    
    
    public Conexion(){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost:3306/DBTonysKinal2019145?useSSL=false","root","admin");
        }catch(ClassNotFoundException e ){
            e.printStackTrace();
        }catch(InstantiationException e){
            e.printStackTrace();    
        }catch(IllegalAccessException e){
            e.printStackTrace();
        }catch(SQLException e){
            e.printStackTrace();
             
       }
    }

    public Connection getConexion() {
        return conexion;
    }

    public void setConexion(Connection conexion) {
        this.conexion = conexion;
    }
    
    public static Conexion getInstance(){
        if (Instancia == null){
            Instancia = new Conexion();
        }
        
        return Instancia;
    }

}
