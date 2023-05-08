package top.pi1grim.ea.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.service.AvatarService;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.SuccessCode;

/**
 * <p>
 * 头像表 前端控制器
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/api/v3/avatar")
@CrossOrigin
@Slf4j
@Tag(name = "头像API", description = "实现头像获取等一系列功能")
public class AvatarController {

    @Resource
    private AvatarService avatarService;

    @Resource
    private TokenService tokenService;

    @GetMapping
    @Operation(summary = "头像获取API", description = "使用GET请求，成功返回用户名，成功代码2085")
    public Response getAvatar(HttpServletRequest request) {
        Long id = tokenService.getId(request);
        log.info("返回学生头像成功 ====> " + id);
        return Response.success(SuccessCode.RETURN_AVATAR_SUCCESS, avatarService.getAvatarList(id));
    }

}
