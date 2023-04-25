package top.pi1grim.ea.service.impl;

import jakarta.annotation.Resource;
import top.pi1grim.ea.component.CrawlerFactory;
import top.pi1grim.ea.service.CrawlerService;

public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    private CrawlerFactory crawlerFactory;


}
