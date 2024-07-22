package com.momo.momopjt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry rhreg) {
    rhreg.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    rhreg.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
    rhreg.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
    rhreg.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
    rhreg.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/");

  }

  // TODO need check  0722 YY
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//    registry.addMapping("/**")
//        .allowedOrigins("http://localhost:8090")
//        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//        .allowedHeaders("*")
//        .allowCredentials(true);
//  }




}
