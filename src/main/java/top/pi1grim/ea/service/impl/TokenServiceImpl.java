package top.pi1grim.ea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.redis.core.BoundKeyOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import top.pi1grim.ea.service.TokenService;

@Service
public class TokenServiceImpl implements TokenService {

    @Resource
    private StringRedisTemplate template;

    public JSONObject getSession(HttpServletRequest request) {
        String token = getToken(request);
        return JSON.parseObject(template.boundValueOps(token).get());
    }

    public String getToken(HttpServletRequest request) {
        return request.getHeader("token");
    }

    public void boundSession(HttpServletRequest request, JSONObject session) {
        template.boundValueOps(getToken(request)).set(session.toString());
    }

    public void sessionPut(HttpServletRequest request, String key, String value) {
        //获取session
        JSONObject session = getSession(request);
        //更新session
        session.put(key, value);
        //更新redis
        boundSession(request, session);
    }
}
