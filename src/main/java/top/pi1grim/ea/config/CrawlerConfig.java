package top.pi1grim.ea.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.pi1grim.ea.component.CrawlerFactory;

@Configuration
public class CrawlerConfig {

    @Bean
    public CrawlerFactory crawlerFactory() {
        return new CrawlerFactory();
    }


}
