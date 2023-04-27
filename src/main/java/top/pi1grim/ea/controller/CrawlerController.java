package top.pi1grim.ea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.service.CrawlerService;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.SuccessCode;

import java.io.File;

@RestController
@CrossOrigin
@RequestMapping("/api/v3/crawler")
@Slf4j
@Tag(name = "Crawler API", description = "实现深度搜索，实时监听等一系列功能")
public class CrawlerController {

    @Resource
    private CrawlerService crawlerService;

    @Resource
    private TokenService tokenService;

    @GetMapping("/login")
    @Operation(summary = "Crawler登录API", description = "使用GET请求，成功返回二维码，成功代码2050")
    public Response login(HttpServletRequest request) {

        Long id = tokenService.getId(request);

        File quick = crawlerService.getQuick(id);
        crawlerService.checkLogin(id);

        return Response.success(SuccessCode.GET_QUICK_SUCCESS, quick);
    }

}
