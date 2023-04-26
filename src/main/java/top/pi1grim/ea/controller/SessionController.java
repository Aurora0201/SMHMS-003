package top.pi1grim.ea.controller;

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
public class SessionController {

    @Resource
    private TokenService tokenService;

    @PutMapping
    public Response put(String key, String value, HttpServletRequest request) {
        tokenService.sessionPut(request, key, value);
        log.info("存储了数据 ====> " + key + " : " + value);
        return Response.success(SuccessCode.SESSION_PUT, key);
    }

    @GetMapping
    public Response get(String key, HttpServletRequest request) {
        log.info("获取了数据 ====> " + key);
        return Response.success(SuccessCode.SESSION_GET, tokenService.sessionGet(request, key));
    }
}
