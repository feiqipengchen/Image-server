package org.example.servlet;

import org.example.dao.ImageDao;
import org.example.model.Image;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/imageShow")
public class ImageShowServlet extends HttpServlet {
    public static final Set<String> whileList=new HashSet<>();
    static {//白名单允许获取图片内容：有两种方式：白名单+黑名单
        whileList.add("http://localhost:8080/image_server/");
        whileList.add("http://localhost:8080/image_server/index.html");
    }
    //显示图片内容
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
          String referer=req.getHeader("Referer");
          if (!whileList.contains(referer)){
              resp.setStatus(403);
              return;
          }
        //获取图片内容
        String id=req.getParameter("imageId");
        Image image= ImageDao.queryOne(Integer.parseInt(id));
        resp.setContentType(image.getContentType());
        String path=ImageServlet.IMAGE_DIR+image.getPath();
        //输入输出
        FileInputStream fileInputStream=new FileInputStream(path);
        OutputStream outputStream=resp.getOutputStream();
        byte[]bytes=new byte[1024];
        int len=0;
        while((len=fileInputStream.read(bytes))!=-1){
           outputStream.write(bytes,0,len);
        }
        outputStream.flush();
        fileInputStream.close();
        outputStream.close();
    }
}
