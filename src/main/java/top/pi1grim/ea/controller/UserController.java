package top.pi1grim.ea.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import top.pi1grim.ea.service.UserService;

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
@Tag(name = "用户API，实现注册，登录和配置修改等一系列功能")
public class UserController {

    @Resource
    private UserService userService;

//    @PostMapping("/register")
//    public
}
