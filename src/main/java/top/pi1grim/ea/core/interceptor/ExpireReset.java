package top.pi1grim.ea.core.interceptor;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import top.pi1grim.ea.core.constant.RedisConstant;
import top.pi1grim.ea.core.constant.StringConstant;


import java.util.concurrent.TimeUnit;

@Slf4j
public class ExpireReset implements HandlerInterceptor {

    @Resource
    private StringRedisTemplate template;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        if(StringConstant.OPTIONS.equals(method))return true;

        String token = request.getHeader(StringConstant.TOKEN);
        if (StringUtils.isNotEmpty(token)) {
            String json = template.boundValueOps(token).get();
            if (StringUtils.isNotEmpty(json)) {
                log.info("刷新TOKEN存活时间 ====> " + json);
                template.boundValueOps(token).expire(RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        }
        return true;
    }
}
