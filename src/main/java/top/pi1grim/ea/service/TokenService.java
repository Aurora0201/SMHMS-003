package top.pi1grim.ea.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    JSONObject getSession(HttpServletRequest request);

    String getToken(HttpServletRequest request);

    void boundSession(HttpServletRequest request, JSONObject session);

    void sessionPut(HttpServletRequest request, String key, Object value);

    String sessionGet(HttpServletRequest request, String key);

    <T> T sessionGet(HttpServletRequest request, String key, Class<T> type);
}
