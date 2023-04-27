package top.pi1grim.ea.component;

import org.springframework.stereotype.Component;

@Component
public class CrawlerFactory {

    public Crawler crawler() {
        return new Crawler().init();
    }
}
