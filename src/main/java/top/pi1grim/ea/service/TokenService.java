package top.pi1grim.ea.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    JSONObject getSession(HttpServletRequest request);

    String getToken(HttpServletRequest request);

    void sessionPut(HttpServletRequest request, String key, String value);

    void sessionPutObject(HttpServletRequest request, String key, Object value);

    String sessionGet(HttpServletRequest request, String key);

    <T> T sessionGetObject(HttpServletRequest request, String key, Class<T> type);
}
