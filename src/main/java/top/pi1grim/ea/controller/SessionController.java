package top.pi1grim.ea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.SuccessCode;

@RestController
@CrossOrigin
@RequestMapping("/api/v3/session")
@Slf4j
@Tag(name = "Session API", description = "提供Session存取数据等基本功能")
public class SessionController {

    @Resource
    private TokenService tokenService;

    @PutMapping
    @Operation(summary = "存储数据", description = "使用PUT请求，查询字符串携带key-value，成功代码2020")
    public Response put(String key, String value, HttpServletRequest request) {
        tokenService.sessionPut(request, key, value);
        log.info("存储了数据 ====> " + key + " : " + value);
        return Response.success(SuccessCode.SESSION_PUT, key);
    }

    @GetMapping
    @Operation(summary = "获取数据", description = "使用GET请求，查询字符串携带key，成功代码2025")
    public Response get(String key, HttpServletRequest request) {
        log.info("获取了数据 ====> " + key);
        return Response.success(SuccessCode.SESSION_GET, tokenService.sessionGet(request, key));
    }
}
