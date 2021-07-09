package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Util {
    private static  final ObjectMapper M=new ObjectMapper();
    private static final MysqlDataSource DS=new MysqlDataSource();
    static {
        DS.setURL("jdbc:mysql://localhost:3306/image_system");
        DS.setUser("root");
        DS.setPassword("root");
        DS.setUseSSL(false);
        DS.setCharacterEncoding("UTF-8");
    }
    public static String serialize(Object o){
        try {
            return M.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw  new RuntimeException("序列化JAVA对象失败"+o,e);
        }
    }
    public static Connection getConnection(){
        try {
            return DS.getConnection();
        } catch (SQLException throwables) {
          throw new RuntimeException("数据库连接失败",throwables);
        }
    }
 public static void close(Connection c, Statement s, ResultSet rs){
     try {
         if (rs!=null)
             rs.close();
         if (s!=null)
             s.close();
         if (c!=null)
             c.close();
     } catch (SQLException throwables) {
         new RuntimeException("数据库资源释放失败",throwables);
     }
 }
    public static void close(Connection c, Statement s){
      close(c,s,null);
    }
}
