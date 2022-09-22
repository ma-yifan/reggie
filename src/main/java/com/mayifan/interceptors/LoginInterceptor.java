package com.mayifan.interceptors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.mayifan.common.BaseContext;
import com.mayifan.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //获取session中的id
        String uri = request.getRequestURI();
        log.info("登录拦截器拦截的请求-->>{}",uri);
//        靠request.js的响应拦截来拦截有bug,如果没有加载是没办法拦截的
//        R<Object> r = R.error("NOTLOGIN");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(r));
        if (BaseContext.getCurrentId()==null&&(Long)request.getSession().getAttribute("employee")!=null) {
            Long emp_id = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(emp_id);
            return true;
        }
        if (BaseContext.getCurrentId()==null&&(Long)request.getSession().getAttribute("user")!=null) {
            Long user_id = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(user_id);
            return true;
        }
        if (uri.contains("/backend")) {
            response.sendRedirect("/backend/page/login/login.html");
        }else if (uri.contains("/front")) {
            response.sendRedirect("/front/page/login.html");
        }

        return false;
    }
}
