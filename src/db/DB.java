package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection connection = null;

    public static Connection getConnection(){
        if(connection == null){
            try {
                Properties properties = loadProperties();
                String url = properties.getProperty("dburl");
                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException exception){
                throw new DBException(exception.getMessage());
            }
        }

        return connection;
    }

    public static void closeConnection(){
        if(connection != null){
            try{
                connection.close();
            } catch(SQLException exception){
                throw new DBException(exception.getMessage());
            }
        }
    }

    public static void closeStatement(Statement statement){
        try{
            if(statement != null){
                statement.close();
            }
        } catch (SQLException exception){
            throw new DBException(exception.getMessage());
        }
    }

    public static void closeResultSet(ResultSet set){
        try{
            if(set != null){
                set.close();
            }
        } catch (SQLException exception){
            throw new DBException(exception.getMessage());
        }
    }

    public static Properties loadProperties(){
        try(FileInputStream fileInputStream = new FileInputStream("db.properties")){
            Properties properties = new Properties();
            properties.load(fileInputStream);
            return properties;
        } catch (IOException exception){
            throw new DBException(exception.getMessage());
        }
    }
}
