package org.example.filter;

import org.example.util.Util;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
/*
用户会话的统一管理:基于过滤器实现
可以统一的进行权限管理，过滤敏感信息等等
*/

@WebFilter("/*")
public class LoginFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        //获取请求路径，判断是否为敏感资源，如果是，需要校验Session
        String uri = httpServletRequest.getServletPath();//应用上下文路径后边的服务路径
        //前端敏感资源/index.html，后端敏感资源/image,/imageShow
        if (uri.equals("/index.html")&&!isLogin(httpServletRequest)){
            //真实地需要获取绝对路径
            httpServletRequest.getScheme();//HTTP
            httpServletRequest.getServerName();//ip或域名
            httpServletRequest.getServerPort();//port
            httpServletRequest.getContextPath();//应用上下文
            httpServletResponse.sendRedirect("login.html");
            return;
        }else if ((uri.equals("/image")||uri.equals("/imageShow"))&&!isLogin(httpServletRequest)){
            httpServletRequest.setCharacterEncoding("UTF-8");
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setContentType("application/json");
            httpServletResponse.setStatus(401);
            Map<String,Object>map=new HashMap<>();
            map.put("ok",false);
            map.put("msg","用户未登录，不允许访问");
            httpServletResponse.getWriter().println(Util.serialize(map));
            return;
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);

    }

    @Override
    public void destroy() {

    }
    public static  boolean isLogin(HttpServletRequest httpServletRequest){
        HttpSession httpSession=httpServletRequest.getSession(false);
        if (httpSession!=null&&httpSession.getAttribute("username")!=null){
            return true;
        }else
            return false;
    }
}
