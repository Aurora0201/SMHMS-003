package top.pi1grim.ea.service.impl;

import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import top.pi1grim.ea.common.utils.EntityUtils;
import top.pi1grim.ea.component.Crawler;
import top.pi1grim.ea.dto.*;
import top.pi1grim.ea.entity.Student;
import top.pi1grim.ea.entity.User;
import top.pi1grim.ea.service.*;
import top.pi1grim.ea.type.CrawlerStatus;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    private StudentService studentService;

    @Resource
    private UserService userService;

    @Resource
    private AvatarService avatarService;

    @Resource
    private ResultService resultService;

    @Resource
    private SmsService smsService;

    @Resource
    private RestTemplate template;

    private static final String URL = "http://api.pi1grim.top/ea/api/v3/python/analysis";

    private List<ResultDTO> unwrap(List<ResultDTOWrapper> wrapperList) {
        return wrapperList.stream().map(this::unwrap).toList();
    }

    private ResultDTO unwrap(ResultDTOWrapper wrapper) {
        List<Integer> time = wrapper.getPostTime();
        return new ResultDTO()
                .setQqNumber(wrapper.getQqNumber())
                .setContent(wrapper.getContent())
                .setNotes(wrapper.getNotes())
                .setUserId(wrapper.getUserId())
                .setPostTime(LocalDateTime.of(time.get(0), time.get(1), time.get(2), time.get(3), time.get(4)))
                .setStuId(wrapper.getStuId())
                .setDataType(wrapper.getDataType())
                .setStatus(wrapper.getStatus())
                .setScore(wrapper.getScore());
    }

    public byte[] getQuick(Long id) {
        Crawler crawler = Crawler.getInstance();

        List<Student> students = studentService.listSelectedByUserId(id);

        Map<String, NumberDTO> map = new HashMap<>();
        students.forEach(student -> {
            NumberDTO dto = new NumberDTO();
            EntityUtils.assign(dto, student);
            map.put(student.getQqNumber(), dto);
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
        List<ResultDTO> result = crawler.deepSearch();
        String response = template.postForObject(URL, result, String.class);
        List<ResultDTOWrapper> dtoList = JSON.parseArray(response, ResultDTOWrapper.class);
        if (Objects.nonNull(dtoList)) {
            List<ResultDTO> unwrap = unwrap(dtoList);
            unwrap.forEach(resultService::insResult);
        }
        log.info("深度数据插入完成 ====> " + id);
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

            String response = template.postForObject(URL, result, String.class);
            List<ResultDTOWrapper> dtoList = JSON.parseArray(response, ResultDTOWrapper.class);
            if (Objects.nonNull(dtoList)) {
                List<ResultDTO> unwrap = unwrap(dtoList);
                ResultDTO dto = unwrap.get(0);

                if (!dto.getStatus().equals("良好")) {
                    User user = userService.getById(id);
                    String stu = "[" + dto.getQqNumber() + "]" + "(" + dto.getNotes() + ")";
                    smsService.sendShortMessage(stu, dto.getStatus(), user.getPhone());
                }

                resultService.insResult(dto);
                log.info("监听数据插入完成 ====> " + id);
            }


        }
    }

    @Async
    public void updateAvatar(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        List<AvatarDTO> avatars = crawler.updateAvatar();
        avatarService.insAvatar(avatars);
    }

    @Async
    public void recycle(Long id) {
        Crawler crawler = Crawler.getCrawler(id);
        crawler.recycle();
    }
}
