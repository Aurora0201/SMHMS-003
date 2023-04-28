package top.pi1grim.ea.service.impl;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.pi1grim.ea.common.utils.EntityUtils;
import top.pi1grim.ea.component.Crawler;
import top.pi1grim.ea.component.CrawlerFactory;
import top.pi1grim.ea.dto.NumberDTO;
import top.pi1grim.ea.dto.ResultDTO;
import top.pi1grim.ea.entity.Student;
import top.pi1grim.ea.entity.User;
import top.pi1grim.ea.service.CrawlerService;
import top.pi1grim.ea.service.StudentService;
import top.pi1grim.ea.service.UserService;
import top.pi1grim.ea.type.CrawlerStatus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    private CrawlerFactory crawlerFactory;

    @Resource
    private StudentService studentService;

    @Resource
    private UserService userService;

    public byte[] getQuick(Long id) {
        Crawler crawler = crawlerFactory.crawler();

        List<Student> students = studentService.listSelectedByUserId(id);

        Map<String, NumberDTO> map = new HashMap<>();
        students.forEach(student -> {
            NumberDTO dto = new NumberDTO();
            EntityUtils.assign(dto, student);
            map.put(student.getNumber(), dto);
        });

        User user = userService.getById(id);

        crawler.register(id, map, user.getStep());
        File quickFile = crawler.getQuick();

        byte[] quickBytes = null;

        try {
            quickBytes = FileUtils.readFileToByteArray(quickFile);
        } catch (IOException e) {
            log.error("转换时发生错误", e);
        }

        return quickBytes;
    }

    @Async
    public void checkLogin(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        crawler.checkLogin();
    }

    public CrawlerStatus getStatus(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        return Objects.isNull(crawler) ? CrawlerStatus.NOT_CREATED : crawler.status();
    }

    public void destroy(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        crawler.destroy();
    }

    @Async
    public void deepSearch(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        List<ResultDTO> results = crawler.deepSearch();
        //TODO:从这里发送到Python
    }

    @Async
    public void listen(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        crawler.listen();
    }

    @Async
    public void executeListen(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        if (crawler.status().equals(CrawlerStatus.LISTEN)) {
            ResultDTO result = crawler.scan();
            if(Objects.isNull(result))return;

            //TODO:发送到Python
        }
    }
}
