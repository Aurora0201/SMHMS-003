package top.pi1grim.ea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.pi1grim.ea.core.interceptor.ExpireReset;
import top.pi1grim.ea.core.interceptor.TokenCheck;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public ExpireReset expireReset() {
        return new ExpireReset();
    }

    @Bean
    public TokenCheck tokenCheck() {
        return new TokenCheck();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        String[] includePath = new String[]{

        };

        String[] excludePath = new String[]{

        };

        registry.addInterceptor(expireReset()).addPathPatterns("/**");
//        registry.addInterceptor(tokenCheck()).addPathPatterns(includePath).excludePathPatterns(excludePath);
    }
}
