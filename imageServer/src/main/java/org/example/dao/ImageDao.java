package org.example.dao;

import org.example.model.Image;
import org.example.util.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ImageDao {
    //检验图片是否存在
    public static int queryCount(String md5){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        try {
            connection= Util.getConnection();
            String sql="select count(0) c from image_table where md5=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,md5);
            resultSet=preparedStatement.executeQuery();
            resultSet.next();
            return resultSet.getInt("c");
        } catch (Exception e) {
            throw new RuntimeException("查询上传图片MD5失败"+md5,e);
        } finally {
            Util.close(connection,preparedStatement,resultSet);
        }
    }
//新增图片
    public static int insert(Image image) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        try {
            connection= Util.getConnection();
            String sql="insert into image_table values(null,?,?,?,?,?,?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,image.getImageName());
            preparedStatement.setLong(2,image.getSize());
            preparedStatement.setString(3,image.getUploadTime());
            preparedStatement.setString(4,image.getMd5());
            preparedStatement.setString(5,image.getContentType());
            preparedStatement.setString(6,image.getPath());
            return preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException("新增上传图片MD5失败",e);
        } finally {
            Util.close(connection,preparedStatement);
        }
    }
//查询图片
    public static List<Image> queryAll() {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        try {
            connection= Util.getConnection();
            String sql="select * from image_table";
            preparedStatement=connection.prepareStatement(sql);
            resultSet=preparedStatement.executeQuery();
            List<Image>result=new ArrayList<>();
            while(resultSet.next()){
                Image image=new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setSize(resultSet.getLong("size"));
                image.setUploadTime(resultSet.getString("upload_time"));
                image.setMd5(resultSet.getString("md5"));
                image.setContentType(resultSet.getString("content_type"));
                image.setPath(resultSet.getString("path"));
                result.add(image);
            };
            return result;
        } catch (Exception e) {
            throw new RuntimeException("查询所有图片出错",e);
        } finally {
            Util.close(connection,preparedStatement,resultSet);
        }
    }
//条件查询图片
    public static Image queryOne(int parseInt) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;
        try {
            connection= Util.getConnection();
            String sql="select * from image_table where image_id=?";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,parseInt);
            resultSet=preparedStatement.executeQuery();
            Image image=null;
            if(resultSet.next()){
                image=new Image();
                image.setImageId(resultSet.getInt("image_id"));
                image.setImageName(resultSet.getString("image_name"));
                image.setSize(resultSet.getLong("size"));
                image.setUploadTime(resultSet.getString("upload_time"));
                image.setMd5(resultSet.getString("md5"));
                image.setContentType(resultSet.getString("content_type"));
                image.setPath(resultSet.getString("path"));
            };
            return image;
        } catch (Exception e) {
            throw new RuntimeException("查询所有图片出错",e);
        } finally {
            Util.close(connection,preparedStatement,resultSet);
        }
    }

    public static int delete(Integer imageId) {
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet = null;

        try {
            connection= Util.getConnection();
            connection.setAutoCommit(false);
            String sql="delete  from image_table where image_id=?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1,imageId);
            return preparedStatement.executeUpdate();
        } catch (SQLException throwables) {
            throw  new RuntimeException("删除图片出错"+imageId,throwables);
        } finally {
            Util.close(connection,preparedStatement,resultSet);
        }
    }


}
