package top.pi1grim.ea.service.impl;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
        JSONObject session = getSession(request);
        session.put(key, value);
        boundSession(request, session);
    }

    public void sessionPutObject(HttpServletRequest request, String key, Object value) {
        JSONObject session = getSession(request);
        session.put(key, JSON.toJSONString(value));
        boundSession(request, session);
    }

    public String sessionGet(HttpServletRequest request, String key) {
        JSONObject session = getSession(request);
        return session.getString(key);
    }

    public <T> T sessionGetObject(HttpServletRequest request, String key, Class<T> type) {
        JSONObject session = getSession(request);
        return session.getObject(key, type);
    }
}
