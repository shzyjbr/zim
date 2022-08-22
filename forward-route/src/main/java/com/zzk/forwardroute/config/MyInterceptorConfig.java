package com.zzk.forwardroute.config;

import com.zzk.forwardroute.interceptor.JWTInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private JWTInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> pathPatterns=new ArrayList<>();
        pathPatterns.add("/**"); //拦截URL
        List<String> excludePathPatterns=new ArrayList<>();
        excludePathPatterns.add("/registerAccount/**");  //不拦截URL
        excludePathPatterns.add("/offLine/**");  //不拦截URL
        excludePathPatterns.add("/login/**");  //不拦截URL
        registry.addInterceptor(jwtInterceptor) //添加拦截器
                .addPathPatterns(pathPatterns) //添加拦截url
                .excludePathPatterns(excludePathPatterns); //添加不拦截url
    }
}
