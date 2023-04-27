package top.pi1grim.ea.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.pi1grim.ea.common.response.Response;

@RestController
@CrossOrigin
@RequestMapping("/api/v3/crawler")
@Slf4j
public class CrawlerController {

    @GetMapping
    public Response login(HttpServletRequest request) {
        return null;
    }

}
