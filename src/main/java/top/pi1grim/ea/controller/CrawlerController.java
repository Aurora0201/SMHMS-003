package top.pi1grim.ea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.exception.CrawlerException;
import top.pi1grim.ea.service.CrawlerService;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.CrawlerStatus;
import top.pi1grim.ea.type.ErrorCode;
import top.pi1grim.ea.type.SuccessCode;

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

        byte[] quick = crawlerService.getQuick(id);
        log.info("二维码获取完成 ====> " + id);
        crawlerService.checkLogin(id);
        log.info("登录检测开始 ====> " + id);

        return Response.success(SuccessCode.GET_QUICK_SUCCESS, quick);
    }

    @GetMapping("/status")
    @Operation(summary = "Crawler状态API", description = "使用GET请求，成功返回Crawler状态，成功代码2055")
    public Response status(HttpServletRequest request) {

        Long id = tokenService.getId(request);
        log.info("返回状态成功 ====> " + id);
        return Response.success(SuccessCode.GET_STATUS_SUCCESS, crawlerService.getStatus(id));
    }

    @DeleteMapping
    @Operation(summary = "Crawler终止API", description = "使用DELETE请求，成功返回id，成功代码2060")
    public Response abort(HttpServletRequest request) {

        Long id = tokenService.getId(request);
        crawlerService.destroy(id);
        log.info("Crawler终止成功 ====> " + id);
        return Response.success(SuccessCode.STOP_CRAWLER_SUCCESS, id);
    }

    @GetMapping("/deep")
    @Operation(summary = "Crawler深度搜索API", description = "使用GET请求，成功启动返回id，成功代码2065")
    public Response deepSearch(HttpServletRequest request) {
        Long id = tokenService.getId(request);

        CrawlerStatus status = crawlerService.getStatus(id);
        if (!status.equals(CrawlerStatus.LEAVE_UNUSED) && !status.equals(CrawlerStatus.LISTEN)) {
            throw new CrawlerException(ErrorCode.WRONG_EXECUTE_TIMING, status);
        }

        crawlerService.deepSearch(id);
        log.info("Crawler深度搜索启动成功 ====> " + id);
        return Response.success(SuccessCode.START_DEEP_SUCCESS, id);
    }

    @GetMapping("/listen")
    @Operation(summary = "Crawler实时监听API", description = "使用GET请求，成功启动返回id，成功代码2070")
    public Response listen(HttpServletRequest request) {
        Long id = tokenService.getId(request);

        CrawlerStatus status = crawlerService.getStatus(id);
        if(!status.equals(CrawlerStatus.LEAVE_UNUSED)){
            throw new CrawlerException(ErrorCode.WRONG_EXECUTE_TIMING, status);
        }

        crawlerService.listen(id);
        log.info("Crawler实时监听启动成功 ====> " + id);
        return Response.success(SuccessCode.START_LISTEN_SUCCESS, id);
    }
}
