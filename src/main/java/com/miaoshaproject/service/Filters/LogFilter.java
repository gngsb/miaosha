package com.miaoshaproject.service.Filters;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.miaoshaproject.error.BusinessException;
import com.miaoshaproject.error.EmBusinessError;
import com.miaoshaproject.response.CommonReturnType;
import com.miaoshaproject.service.model.UserModel;
import net.sf.json.JSONObject;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class LogFilter extends FilterRegistrationBean<Filter> {
    @Override
    public Filter getFilter() {
        return new AuthFilter();
    }

    @PostConstruct
    public void init() {
        setFilter(new AuthFilter());
        setUrlPatterns(List.of("/item/*"));
    }
    @CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
    class AuthFilter implements Filter {

        @Override
        public void init(FilterConfig filterConfig) throws ServletException {

        }

        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            //System.out.println("123");
            HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
            HttpServletResponse httpServletResponse = (HttpServletResponse)servletResponse;


            //获取用户的登录信息
            Boolean isLogin = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
            if (isLogin==null || !isLogin.booleanValue()){
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json; charset=utf-8");
                PrintWriter out = httpServletResponse.getWriter();
                //解决跨域
                String originHeader = httpServletRequest.getHeader("Origin");
                httpServletResponse.setHeader("Access-Control-Allow-Origin", originHeader);
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
                httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
                httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,authorization");
                httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("XDomainRequestAllowed","1");
                httpServletResponse.setHeader("XDomainRequestAllowed","1");

                Map<String,Object> responseData = new HashMap<>();
                responseData.put("errCode", EmBusinessError.USER_NOT_LOGIN.getErrCode());
                responseData.put("errMsg",EmBusinessError.USER_NOT_LOGIN.getErrMsg());
                JSONObject responseJSONObject = JSONObject.fromObject(CommonReturnType.create(responseData,"fail"));
//                responseJSONObject.put("errCode", EmBusinessError.USER_NOT_LOGIN.getErrCode());
//                responseJSONObject.put("errMsg",EmBusinessError.USER_NOT_LOGIN.getErrMsg());
//                httpServletResponse.sendError(EmBusinessError.USER_NOT_LOGIN.getErrCode(),EmBusinessError.USER_NOT_LOGIN.getErrMsg());
//                httpServletResponse.setHeader("refresh",EmBusinessError.USER_NOT_LOGIN.getErrMsg());
                out.append(responseJSONObject.toString());
                out.flush();
                out.close();
                //return;
            }else{
                filterChain.doFilter(servletRequest,servletResponse);
            }

        }

        @Override
        public void destroy() {

        }
    }
}
