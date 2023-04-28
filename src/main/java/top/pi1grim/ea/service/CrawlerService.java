package top.pi1grim.ea.service;

import top.pi1grim.ea.type.CrawlerStatus;



public interface CrawlerService {
    byte[] getQuick(Long id);

    void checkLogin(Long id);

    CrawlerStatus getStatus(Long id);

    void destroy(Long id);

    void deepSearch(Long id);

    void listen(Long id);
}
