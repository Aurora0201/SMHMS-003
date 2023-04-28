package top.pi1grim.ea.component;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import top.pi1grim.ea.dto.ResultDTO;
import top.pi1grim.ea.service.CrawlerService;
import top.pi1grim.ea.type.CrawlerStatus;

import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class ScheduleTask {

    @Resource
    private CrawlerService crawlerService;

    @Scheduled(cron = "0/15 * *  * * ? ")
    public void executeScan() {
        for (Map.Entry<Long, Crawler> entry : Crawler.getCrawlerMap().entrySet()) {
            Long id = entry.getKey();
            crawlerService.executeListen(id);
        }
    }

}
