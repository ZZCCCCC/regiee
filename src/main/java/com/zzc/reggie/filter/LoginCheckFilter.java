package com.zzc.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.zzc.reggie.common.BaseContext;
import com.zzc.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: 赵智超
 * @date: 2023/02/18/16:10
 * @Description: 检查用户是否登录
 */
@WebFilter(filterName = "loginCheckFilter", urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {
    //路径匹配器,支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse respones = (HttpServletResponse) servletResponse;
        //1.获取本次请求的uri
        String requestURI = request.getRequestURI();

        //不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        //2.判断本次请求是否需要处理
        boolean check = check(urls, requestURI);
        //3.不需要处理
        if (check) {
            filterChain.doFilter(request, respones);
            return;
        }
        //4.判断登录状态,如已登录,则放行
        if (request.getSession().getAttribute("employee") != null) {

            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, respones);
            return;
        }

        if (request.getSession().getAttribute("user") != null) {
            long empId = Long.parseLong((String) request.getSession().getAttribute("user"));
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request, respones);
            return;
        }
        //5.未登录,向客户端写数据
        respones.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;
        //log.info("拦截到请求:{}", request.getRequestURI());
    }

    /**
     * 路径匹配,检查此次请求是否放行
     *
     * @param urls
     * @param requestURI
     * @return
     */
    public boolean check(String[] urls, String requestURI) {
        for (String uri : urls) {
            boolean match = PATH_MATCHER.match(uri, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }




}
