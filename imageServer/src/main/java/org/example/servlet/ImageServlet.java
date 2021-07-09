package org.example.servlet;

import org.apache.commons.codec.digest.DigestUtils;
import org.example.dao.ImageDao;
import org.example.exception.AppException;
import org.example.model.Image;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/image")
@MultipartConfig//Part配合使用
public class ImageServlet extends HttpServlet {
    public static  String IMAGE_DIR="E://TMP";
    @Override
    //上传图片
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, Object> hashMap = new HashMap<>();
        try {
            req.setCharacterEncoding("UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");

            //获取图片信息
            Part filename = req.getPart("uploadImage");
            long size = filename.getSize();
            String contentType = filename.getContentType();
            InputStream inputStream = filename.getInputStream();
            //根据输入流转MD5校验码
            String md5= DigestUtils.md5Hex(inputStream);
            if(ImageDao.queryCount(md5)>=1)
                throw new AppException("上传图片重复");
            //保存图片内容
            filename.write("E://TMP/"+md5);

            String submittedFileName = filename.getSubmittedFileName();
            DateFormat df=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String uploadTime=df.format(new Date());
//设置实体类信息
            Image image=new Image();
            image.setImageName(submittedFileName);
            image.setContentType(contentType);
            image.setSize(size);
            image.setUploadTime(uploadTime);
            image.setMd5(md5);
            image.setPath("/"+md5);
//插入图片
            int n=ImageDao.insert(image);
            hashMap.put("ok",true);
        } catch (Exception e) {
            hashMap.put("ok",false);
            if (e instanceof AppException){
                hashMap.put("msg",e.getMessage());
            }
            else{
                hashMap.put("msg","未知错误，联系管理员");
            }

           // resp.setStatus(500);
        }
        resp.getWriter().println(Util.serialize(hashMap));
     /*   ObjectMapper result=new ObjectMapper();
        Map<String,Object> a=new HashMap<>();
        a.put("size",size/1024);
        a.put("contentType",contentType);
        a.put("submittedFileName",submittedFileName);
        resp.getWriter().println(result.writeValueAsString(a));*/

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id=req.getParameter("imageId");
        Object o;
        if (id==null){
           o=ImageDao.queryAll();
        }
        else {
            int i=Integer.parseInt(id);
            o=ImageDao.queryOne(i);

        }
        resp.getWriter().println(Util.serialize(o));
    }
    //删除图片
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        String id=req.getParameter("imageId");

        //数据库文件信息删除
        Image image=ImageDao.queryOne(Integer.parseInt(id));
        int n=ImageDao.delete(image.getImageId());
        //磁盘文件删除
        String path=IMAGE_DIR+image.getPath();
        File file=new File(path);
        file.delete();
        resp.getWriter().println("{\"ok\":true}");
    }
}
