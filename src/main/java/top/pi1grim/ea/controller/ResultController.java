package top.pi1grim.ea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.entity.Result;
import top.pi1grim.ea.service.ResultService;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.SuccessCode;

import java.util.List;

/**
 * <p>
 * 结果表 前端控制器
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@RestController
@RequestMapping("/api/v3/result")
@CrossOrigin
@Slf4j
@Tag(name = "结果集API", description = "实现结果集获取等一系列功能")
public class ResultController {

    @Resource
    private ResultService resultService;

    @Resource
    private TokenService tokenService;

    @GetMapping

    public Response result(HttpServletRequest request) {
        Long id = tokenService.getId(request);
        List<Result> results = resultService.list(new LambdaQueryWrapper<Result>()
                .eq(Result::getUserId, id)
                .orderByDesc(Result::getPostTime));

        return Response.success(SuccessCode.RETURN_RESULT_SUCCESS, results);

    }

}
