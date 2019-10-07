package com.mr.nanke.config.web;

import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.mr.nanke.interceptor.shopadmin.ShopLoginInterceptor;
import com.mr.nanke.interceptor.shopadmin.ShopPermissionInterceptor;

import org.omg.PortableInterceptor.Interceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

/**
 * WebMvcConfigurerAdapter:配置视图解析器viewResolver   
 * ApplicationContextAware接口：方便获取ApplicationContext中所有的bean
 * @author 夏小颜
 */
@SuppressWarnings("deprecation")
@Configuration
//<mvc:annotation-driven />开启SpringMVC注解驱动
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationContextAware{

    //spring容器
    private ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
    
    //静态资源配置
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        //registry.addResourceHandler("/resources/**").addResourceLocations("classpath:/resources/");
        //一定要加file spring boot内置tomcat，无法修改。。
        registry.addResourceHandler("/upload/**").addResourceLocations("file:D:/projectdev/upload/");
    }
    
    //定义默认的请求处理器
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
    
    //创建视图解析器viewResolver
    @Bean(name = "viewResolver")
    public ViewResolver createViewResolver(){
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        //设置Spring容器
        viewResolver.setApplicationContext(this.applicationContext);
        //取消缓存
        viewResolver.setCache(false);
        //设置解析的前缀
        viewResolver.setPrefix("/WEB-INF/html/");
        //设置视图解析的后缀
        viewResolver.setSuffix(".html");
        return viewResolver;
    }

    //文件上传解析器
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver createMultipartResolver(){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setDefaultEncoding("utf-8");
        //1024*1024*20 = 20M
        multipartResolver.setMaxUploadSize(20971520);
        multipartResolver.setMaxInMemorySize(20971520);
        return multipartResolver;
    }
    
    
    @Value("${kaptcha.border}")
    private String border;

    //todo ....kaptcha待全部映入。。 都是String
    @Value("${kaptcha.textproducer.font.color}")
    private String fontColor;
    
    @Value("${kaptcha.image.width}")
    private String width;
    
    @Value("${kaptcha.image.height}")
    private String height;
    
    @Value("${kaptcha.textproducer.char.string}")
    private String textproducer;
    
    @Value("${kaptcha.textproducer.font.size}")
    private String fontSize;
    
    @Value("${kaptcha.noise.color}")
    private String noiseColor;
    
    @Value("${kaptcha.textproducer.char.length}")
    private String length;
    
    @Value("${kaptcha.textproducer.font.name}")
    private String fontName;
    
    //由于springboot中无web.xml，需要在这里配置Kaptcha验证码Servlet
    @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean servlet = new ServletRegistrationBean(new KaptchaServlet(),"/Kaptcha");
        servlet.addInitParameter("kaptcha.border",border);
        //do
        servlet.addInitParameter("kaptcha.textproducer.font.color", fontColor);
        servlet.addInitParameter("kaptcha.image.width", width);
        servlet.addInitParameter("kaptcha.textproducer.char.string", textproducer);
        servlet.addInitParameter("kaptcha.image.height", height);
        servlet.addInitParameter("kaptcha.textproducer.font.size", fontSize);
        servlet.addInitParameter("kaptcha.noise.color", noiseColor);
        servlet.addInitParameter("kaptcha.textproducer.char.length", length);
        servlet.addInitParameter("kaptcha.textproducer.font.name", fontName);
        
        return servlet;
    }
    
    
    //添加拦截器配置
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String interceptorsPath = "/shopadmin/**";
        //注册拦截器
        InterceptorRegistration loginIR = registry.addInterceptor(new ShopLoginInterceptor());
        //配置拦截路径
        loginIR.addPathPatterns(interceptorsPath);
        //还可以注册其它拦截器
        InterceptorRegistration permisssionIR = registry.addInterceptor(new ShopPermissionInterceptor());
        //配置拦截路径
        permisssionIR.addPathPatterns(interceptorsPath);
        //配置不拦截的路径
        permisssionIR.excludePathPatterns("/shopadmin/shoplist");
        permisssionIR.excludePathPatterns("/shopadmin/getshoplist");
        permisssionIR.excludePathPatterns("/shopadmin/shopoperation");
//        permisssionIR.excludePathPatterns("/local/changepsw");
//        permisssionIR.excludePathPatterns("/local/changelocalauthpwd");
//        permisssionIR.excludePathPatterns("/local/bindlocal");
//        permisssionIR.excludePathPatterns("/local/bindlocalauth");
//        permisssionIR.excludePathPatterns("/local/login");
//        permisssionIR.excludePathPatterns("/local/logincheck");
//        permisssionIR.excludePathPatterns("/local/logout");
        permisssionIR.excludePathPatterns("/shopadmin/shopmanager");
        permisssionIR.excludePathPatterns("/shopadmin/getmanagerinfo");
//        permisssionIR.excludePathPatterns("/shopadmin/modifyshop");
//        permisssionIR.excludePathPatterns("/shopadmin/getInfoById");
        permisssionIR.excludePathPatterns("/shopadmin/getshopinitinfo");
        permisssionIR.excludePathPatterns("/shopadmin/registershop");
//        permisssionIR.excludePathPatterns("/shopadmin/productmanage");
//        permisssionIR.excludePathPatterns("/shopadmin/productoperation");
//        permisssionIR.excludePathPatterns("/shopadmin/getproductlistbypage");
//        permisssionIR.excludePathPatterns("/shopadmin/modifyproduct");
//        permisssionIR.excludePathPatterns("/shopadmin/addproduct");
//        permisssionIR.excludePathPatterns("/shopadmin/getproductbyid");
//        permisssionIR.excludePathPatterns("/shopadmin/getproductcategorylist");
//        permisssionIR.excludePathPatterns("/shopadmin/productcategorymanage");
    }
}
