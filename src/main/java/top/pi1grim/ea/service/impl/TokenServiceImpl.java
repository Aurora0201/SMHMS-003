package top.pi1grim.ea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.pi1grim.ea.service.TokenService;

public class TokenServiceImpl implements TokenService {

    @Resource
    private StringRedisTemplate template;

    public JSONObject getSession(HttpServletRequest request) {
        String token = request.getHeader("token");
        return JSON.parseObject(template.boundValueOps(token).get());
    }
}
