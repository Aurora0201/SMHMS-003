package top.pi1grim.ea.service.impl;

import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.pi1grim.ea.component.Crawler;
import top.pi1grim.ea.component.CrawlerFactory;
import top.pi1grim.ea.entity.Student;
import top.pi1grim.ea.service.CrawlerService;
import top.pi1grim.ea.service.StudentService;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    private CrawlerFactory crawlerFactory;

    @Resource
    private StudentService studentService;

    public File getQuick(Long id) {
        Crawler crawler = crawlerFactory.crawler();

        List<Student> students = studentService.listByUserId(id);

        Map<String, String> map = new HashMap<>();
        students.forEach(student -> map.put(student.getNumber(), student.getNotes()));

        crawler.register(id,map);
        return crawler.getQuick();
    }

    @Async
    public void checkLogin(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        crawler.checkLogin();
    }
}
