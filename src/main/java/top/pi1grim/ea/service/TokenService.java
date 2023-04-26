package top.pi1grim.ea.service;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.http.HttpServletRequest;

public interface TokenService {
    JSONObject getSession(HttpServletRequest request);

    String getToken(HttpServletRequest request);

    void sessionPut(HttpServletRequest request, String key, String value);
}
