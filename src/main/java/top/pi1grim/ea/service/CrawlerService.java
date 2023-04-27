package top.pi1grim.ea.service;

import java.io.File;

public interface CrawlerService {
    File getQuick(Long id);

    void checkLogin(Long id);
}
