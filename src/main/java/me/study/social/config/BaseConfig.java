package me.study.social.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class BaseConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry){
        resourceHandlerRegistry.addResourceHandler("**/**/default.jpg").addResourceLocations("classpath:/static/");
        resourceHandlerRegistry.addResourceHandler("img/**/default.jpg").addResourceLocations("classpath:/static/");
        resourceHandlerRegistry.addResourceHandler("**/**.jpg").addResourceLocations("file:D:\\social\\");
        resourceHandlerRegistry.addResourceHandler("img/**.jpg").addResourceLocations("file:D:\\social\\");

    }
}
