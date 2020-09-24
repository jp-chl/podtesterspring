package nectia.com.podtesterspring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
public class SwaggerConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("nectia.com.podtesterspring"))
                .paths(PathSelectors.any())
                .build().apiInfo(apiEndPointInfo());
    } // end Docket api()

    private ApiInfo apiEndPointInfo() {
        return new ApiInfoBuilder().title("Spring Boot microservice for testing purposes API")
                .description("Specially designed for Docker, Kubernetes and Istio testing")
                .contact(new Contact("JP", "", ""))
                .license("")
                .licenseUrl("")
                .version("0.0.1-SNAPSHOT")
                .build();
    } // end ApiInfo apiEndPointInfo()
} // end class SwaggerConfiguration
