package top.pi1grim.ea.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI apiConfig() {
        return new OpenAPI().info(
                new Info().title("学生心理健康管理系统API文档")
                        .version("v3.0.0")
                        .contact(new Contact()
                                .email("1176302809@qq.com"))
        );
    }
}
