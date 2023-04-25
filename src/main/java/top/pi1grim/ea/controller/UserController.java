package top.pi1grim.ea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.common.utils.EntityUtils;
import top.pi1grim.ea.dto.RegisterDTO;
import top.pi1grim.ea.entity.User;
import top.pi1grim.ea.exception.UserException;
import top.pi1grim.ea.service.UserService;
import top.pi1grim.ea.type.ErrorCode;
import top.pi1grim.ea.type.SuccessCode;

import java.time.LocalDateTime;
import java.util.Objects;

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
}
