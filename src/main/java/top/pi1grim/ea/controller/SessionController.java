package top.pi1grim.ea.controller;

import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Resource
    private StringRedisTemplate template;

    @PutMapping
    public Response put(String key, String value, HttpServletRequest request) {
        tokenService.sessionPut(request, key, value);
        return Response.success(SuccessCode.SESSION_PUT, key);
    }

}
