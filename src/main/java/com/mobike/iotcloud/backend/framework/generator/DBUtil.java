package com.mobike.iotcloud.backend.framework.generator;


import com.mobike.iotcloud.backend.framework.util.Configs;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class DBUtil {
    private static Properties dbconfig = new Properties();

    static {
        InputStream in = Configs.class.getClassLoader().getResourceAsStream("generator.properties");
        try {
            dbconfig.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            org.apache.commons.io.IOUtils.closeQuietly(in);
        }
    }


    public static Connection getConn() {
        String driverClassName = dbconfig.getProperty("jdbc.generator.driverClassName");
        String url = dbconfig.getProperty("jdbc.generator.url");
        String username = dbconfig.getProperty("jdbc.generator.username");
        String password = dbconfig.getProperty("jdbc.generator.password");

        try {
            Class.forName(driverClassName);
            return DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }

    }

    public static Table getTable(String className, String tableName, Connection conn) {
        Table table = null;
        try {
            Statement stmt = conn.createStatement();
            ResultSet singleTableRS = stmt.executeQuery("SHOW FULL COLUMNS FROM " + tableName);

            table = new Table(className, null);
            while (singleTableRS.next()) {
                Column column = new Column(singleTableRS.getString("Field"), singleTableRS.getString("Comment"), singleTableRS.getString("Type"), singleTableRS.getString("Null"));
                table.getColumns().add(column);
            }
            singleTableRS.close();
            stmt.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return table;
    }
}
