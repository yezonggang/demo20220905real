package com.quick.start.demo.config.swagger2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 implements WebMvcConfigurer {
     // 配置扫描的包
     @Bean
     public Docket createRestApi() {
          return new Docket(DocumentationType.SWAGGER_2)
                  .apiInfo(apiInfo())
                  .select()
                  .apis(RequestHandlerSelectors.basePackage("com.quick.start.demo.controller")).paths(PathSelectors.any())
                  .build();
     }

     private ApiInfo apiInfo() {
          return new ApiInfoBuilder()
                  .title("测试Swagger的API.")
                  // 创建人信息
                  .contact(new Contact("yzg", "https://blog.csdn.net/yezonggang", "717818895@qq.com"))
                  // 版本号
                  .version("2.0")
                  // 描述
                  .description("描述")
                  .build();
     }


     @Override
     public void addResourceHandlers(ResourceHandlerRegistry registry) {
          registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
          registry.addResourceHandler("swagger-ui.html")
                  .addResourceLocations("classpath:/META-INF/resources/");
          registry.addResourceHandler("/webjars/**")
                  .addResourceLocations("classpath:/META-INF/resources/webjars/");
     }
}