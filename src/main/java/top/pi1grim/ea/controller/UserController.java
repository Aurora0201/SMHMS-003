package top.pi1grim.ea.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.common.utils.EntityUtils;
import top.pi1grim.ea.common.utils.JWTUtils;
import top.pi1grim.ea.core.constant.RedisConstant;
import top.pi1grim.ea.dto.LoginDTO;
import top.pi1grim.ea.dto.RegisterDTO;
import top.pi1grim.ea.entity.User;
import top.pi1grim.ea.exception.UserException;
import top.pi1grim.ea.service.UserService;
import top.pi1grim.ea.type.ErrorCode;
import top.pi1grim.ea.type.SuccessCode;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-25
 */
@RestController
@RequestMapping("/user")
@CrossOrigin
@Slf4j
@Tag(name = "用户API", description = "实现注册，登录和配置修改等一系列功能")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private StringRedisTemplate template;

    @PostMapping("/register")
    @Operation(summary = "用户注册API", description = "使用POST请求，成功返回用户名，成功代码2000")
    public Response register(@RequestBody RegisterDTO dto) {

        if (Objects.isNull(dto) || EntityUtils.fieldIsNull(dto)) {
            throw new UserException(ErrorCode.ILLEGAL_REQUEST_BODY, dto);
        }

        if (Objects.nonNull(userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername())))) {
            throw new UserException(ErrorCode.DUPLICATE_USERNAME, dto.getUsername());
        }

        User user = new User();
        user.setCreateTime(LocalDateTime.now());
        EntityUtils.assign(user, dto);

        userService.save(user);
        log.info("注册成功 ====> " + user);
        return Response.success(SuccessCode.REGISTER_SUCCESS, dto.getUsername());
    }


    @PostMapping("/login")
    @Operation(summary = "用户登录API", description = "使用POST请求，成功返回用户名，成功代码2005")
    public Response register(@RequestBody LoginDTO dto) {

        if (Objects.isNull(dto) || EntityUtils.fieldIsNull(dto)) {
            throw new UserException(ErrorCode.ILLEGAL_REQUEST_BODY, dto);
        }

        User user = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUsername, dto.getUsername()));
        if (Objects.isNull(user)) {
            throw new UserException(ErrorCode.NOT_EXIST_USER, dto.getUsername());
        }

        if (!user.getPassword().equals(dto.getPassword())) {
            throw new UserException(ErrorCode.USERNAME_PASSWORD_MISMATCH, dto.getUsername());
        }

        JSONObject session = new JSONObject();
        session.put("user", user);
        session.put("login_time", Instant.now());

        String token = JWTUtils.genToken(dto.getUsername());

        template.boundValueOps(token).set(session.toString(), RedisConstant.TOKEN_EXPIRE_TIME, TimeUnit.SECONDS);
        log.info("登录成功 ====> " + session);
        return Response.success(SuccessCode.LOGIN_SUCCESS, dto.getUsername());
    }
}
