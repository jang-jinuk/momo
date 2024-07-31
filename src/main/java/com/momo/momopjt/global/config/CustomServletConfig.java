package com.momo.momopjt.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class CustomServletConfig implements WebMvcConfigurer {

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
    registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
    registry.addResourceHandler("/fonts/**").addResourceLocations("classpath:/static/fonts/");
    registry.addResourceHandler("/assets/**").addResourceLocations("classpath:/static/assets/").setCachePeriod(20);
    registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    registry.addResourceHandler("/img/**").addResourceLocations("/static/img/").setCachePeriod(10);

    registry.addResourceHandler("/images/**").addResourceLocations("/Users/yjpark/upload/");
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
