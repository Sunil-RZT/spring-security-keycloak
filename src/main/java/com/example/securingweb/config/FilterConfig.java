//package com.example.securingweb;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FilterConfig
//{
//    @Bean
//    public FilterRegistrationBean<PostAuthActionFilter> myFilter() {
//        FilterRegistrationBean<PostAuthActionFilter> registrationBean = new FilterRegistrationBean<>();
//        registrationBean.setFilter(new PostAuthActionFilter());
//        registrationBean.addUrlPatterns("/*"); // Set URL patterns for your filter
//        registrationBean.setOrder(1); // Set the order
//
//        return registrationBean;
//    }
//}
