package com.mayifan.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.mayifan.common.BaseContext;
import com.mayifan.common.JacksonObjectMapper;
import com.mayifan.interceptors.LoginInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
public class MyConfiguration {

    /**
     * 注册我的自定义配置类
     * @return
     */
    /*
    配置德鲁伊连接池
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    @Bean
    public WebMvcConfigurer MyWebMvcConfigurer(){
        return new WebMvcConfigurer() {
            /*
            配置我的静态资源映射
             */
            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                log.info("开启静态资源映射...");
                registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
                registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
            }
        };
    }
    /*
    MP插件
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        //分页插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        paginationInnerInterceptor.setMaxLimit(500L);
        paginationInnerInterceptor.setOverflow(true);
        paginationInnerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        //乐观锁插件
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

    /**
     * 自动注入管理
     * @return
     */
    @Bean
    public MetaObjectHandler metaObjectHandler(){
        return new MetaObjectHandler() {
            @Override
            public void insertFill(MetaObject metaObject) {
                //创建时间
                if (metaObject.hasSetter("createTime")&&
                        this.getFieldValByName("createTime",metaObject)==null) {
                    log.info("start insert fill_createTime...");
                    this.strictInsertFill(metaObject, "createTime", LocalDateTime.class, LocalDateTime.now());
                }
                //更新时间
                if (metaObject.hasSetter("updateTime")&&
                        this.getFieldValByName("updateTime",metaObject)==null) {
                    log.info("start insert fill_updateTime...");
                    this.strictInsertFill(metaObject, "updateTime", LocalDateTime.class, LocalDateTime.now());
                }
                //创建人id
                if (metaObject.hasSetter("createUser")&&
                        this.getFieldValByName("createUser",metaObject)==null) {
                    log.info("start insert fill_createUser...");
                    this.strictInsertFill(metaObject, "createUser", Long.class,
                            BaseContext.getCurrentId());
                }
                //修改人id
                if (metaObject.hasSetter("updateUser")&&
                        this.getFieldValByName("updateUser",metaObject)==null) {
                    log.info("start insert fill_updateUser...");
                    this.strictInsertFill(metaObject, "updateUser", Long.class,
                            BaseContext.getCurrentId());
                }
                //账户密码
                if (metaObject.hasSetter("password")&&
                        this.getFieldValByName("password",metaObject)==null) {
                    log.info("start insert fill_password...");
                    this.strictInsertFill(metaObject, "password", String.class,
                            DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
                }
                //地址账铺userId
                if (metaObject.hasSetter("userId")&&
                        this.getFieldValByName("userId",metaObject)==null) {
                    log.info("start insert fill_userId...");
                    this.strictInsertFill(metaObject, "userId", Long.class,
                            BaseContext.getCurrentId());
                }
                //下单时间
                if (metaObject.hasSetter("orderTime")&&
                        this.getFieldValByName("orderTime",metaObject)==null) {
                    log.info("start insert fill_orderTime...");
                    this.strictInsertFill(metaObject, "orderTime", LocalDateTime.class, LocalDateTime.now());
                }

                if (metaObject.hasSetter("checkoutTime")&&
                        this.getFieldValByName("checkoutTime",metaObject)==null) {
                    log.info("start insert fill_checkoutTime...");
                    this.strictInsertFill(metaObject, "checkoutTime", LocalDateTime.class, LocalDateTime.now());
                }
            }

            @Override
            public void updateFill(MetaObject metaObject) {
                //上一次更新时间
                if (metaObject.hasSetter("updateTime")&&
                        this.getFieldValByName("updateTime",metaObject)==null) {
                    log.info("start update fill_updateTime...");
                    this.strictUpdateFill(metaObject,"updateTime", LocalDateTime.class,LocalDateTime.now());
                }
                //上次更新用户的id
                if (metaObject.hasSetter("updateUser")&&
                        this.getFieldValByName("updateUser",metaObject)==null) {
                    log.info("start update fill_updateUser...");
                    this.strictUpdateFill(metaObject,"updateUser", Long.class,
                            BaseContext.getCurrentId());
                }
            }
        };
    }

    @Bean
    public WebMvcConfigurer myWebMvcConfigurer (){
        return new WebMvcConfigurer() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
               registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**")
                       .excludePathPatterns(Arrays.asList("/**/api/**","/**/js/**","/**/images/**",
                               "/backend/plugins/**","/**/styles/**","/backend/favicon.ico",
                               "/**/login.html","/employee/login","/user/sendMsg","/user/login",
                               "/front/fonts/**","/error"));
            }
            //扩展我们自己的消息转换器，目前设置json的转换格式
            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                /*
                参考mvc自带的json转换器，我们也可以照猫画虎的创建我们自己的消息转换器
                但是，有一点要改的就是，对于格式的装换我们要设置成我们自己
                 */
                MappingJackson2HttpMessageConverter myJsonMessageConverter = new MappingJackson2HttpMessageConverter();
                //转换类型映射
                myJsonMessageConverter.setObjectMapper(new JacksonObjectMapper());
                //这里还有个要注意的就是，下标写靠前一点，优先进行解析，不写默认是放在最后
                converters.add(0,myJsonMessageConverter);
            }
        };
    }



}
