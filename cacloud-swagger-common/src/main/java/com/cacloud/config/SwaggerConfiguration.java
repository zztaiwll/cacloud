package com.cacloud.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.*;

@Component//Spring扫描
@EnableOpenApi//开启OpenApi规范
@ConfigurationProperties("swagger")//配置前缀
@Data
public class SwaggerConfiguration {
  /**
 * 是否开启swagger，生产环境一般关闭，所以这里定义一个变量
 */
  private Boolean enable;

  /**
 * 项目应用名
 */
  private String applicationName;

  /**
  * 项目版本信息
 */
  private String applicationVersion;

  /**
 * 项目描述信息
  */
  private String applicationDescription;

    @Bean
    public Docket docket() {
      return new Docket(DocumentationType.OAS_30)
              //是否启动
              .enable(enable)
              //头部信息
              .apiInfo(apiInfo())
              .select()
              /**
               * RequestHandlerSelectors,配置要扫描接口的方式
               * basePackage指定要扫描的包
               * any()扫描所有，项目中的所有接口都会被扫描到
               * none()不扫描
               * withClassAnnotation()扫描类上的注解
               * withMethodAnnotation()扫描方法上的注解
               */
              .apis(RequestHandlerSelectors.any())
              //过滤某个路径
              .paths(PathSelectors.any())
              .build()
              //协议
              .protocols(newHashSet("https", "http"))
              .securitySchemes(securitySchemes())
              .securityContexts(securityContexts())
              ;
     }

  private ApiInfo apiInfo() {

    return new ApiInfoBuilder()
       .title(applicationName)
      .description(applicationDescription)
      .contact(new Contact("作者", "网站", "邮箱"))
      .version(applicationVersion)
      .build();
  }
  private List<SecurityScheme> securitySchemes() {
    ApiKey aa=new ApiKey("BASE_TOKEN", "token", "header");
    List<SecurityScheme> result=new ArrayList<>();
    result.add(aa);
    return result;
  }

  /**
   * 授权信息全局应用
   */
  private List<SecurityContext> securityContexts() {
    return Collections.singletonList(
            SecurityContext.builder()
                    .securityReferences(
                            Collections.singletonList(new SecurityReference("BASE_TOKEN",
                                    new AuthorizationScope[]{new AuthorizationScope("global", "")}
                            )))
                    //.forPaths(PathSelectors.any())
                    .build()
    );
  }

  @SafeVarargs
  private final <T> Set<T> newHashSet(T... ts) {
    if (ts.length > 0) {
      return new LinkedHashSet<>(Arrays.asList(ts));
    }
    return null;
  }
}
