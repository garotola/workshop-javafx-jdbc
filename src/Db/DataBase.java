package Db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataBase {

    private static Connection conn = null;

    public static Connection getConnection(){
        if(conn == null){
            try{
                Properties ps = loadProperties();
                String url = ps.getProperty("dburl");
                conn = DriverManager.getConnection(url, ps);
            }catch(SQLException e){throw new DbException(e.getMessage());}
        }
        return conn;
    }

    public static void closeConnection(){
        if(conn != null) {
            try {conn.close();}
            catch(SQLException e){throw new DbException(e.getMessage());}
        }
    }
    
    private static Properties loadProperties(){
        try(FileInputStream fs = new FileInputStream("db.properties")){
            Properties ps = new Properties();
            ps.load(fs);
            return ps;
        }catch(IOException e){
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement st){
        if(st != null){
            try {
                st.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }

    public static void closeResultSet(ResultSet rs){
        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new DbException(e.getMessage());
            }
        }
    }
}
