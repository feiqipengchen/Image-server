package org.example.servlet;



import org.example.exception.AppException;
import org.example.util.Util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/login")
public class LoginServelt extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        Map<String,Object> map=new HashMap<>();
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        List<String> usernames= Arrays.asList("a","b","c");//模拟账号
        try {
            if (!usernames.contains(username)){
                throw new AppException("用户不存在");
            }else if(!"123".equals(password)){
                throw new AppException("账户或密码错误");
            }
            //登入成功
            HttpSession httpSession=req.getSession();
            httpSession.setAttribute("username",username);
            map.put("ok",true);
        } catch (Exception e) {
            map.put("ok",false);
            if (e instanceof AppException){
               map.put("msg",e.getMessage());
            }
            else{
                map.put("msg","未知错误，联系管理员");
            }

        }
        resp.getWriter().println(Util.serialize(map));
    }

}
