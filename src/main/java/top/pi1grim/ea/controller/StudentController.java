package top.pi1grim.ea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.pi1grim.ea.common.response.Response;
import top.pi1grim.ea.common.utils.EntityUtils;
import top.pi1grim.ea.dto.ItemDTO;
import top.pi1grim.ea.dto.StudentDTO;
import top.pi1grim.ea.entity.Student;
import top.pi1grim.ea.exception.StudentException;
import top.pi1grim.ea.service.StudentService;
import top.pi1grim.ea.service.TokenService;
import top.pi1grim.ea.type.ErrorCode;
import top.pi1grim.ea.type.SuccessCode;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 学生表 前端控制器
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/api/v3/student")
@CrossOrigin
@Slf4j
@Tag(name = "学生API", description = "实现添加，修改和删除等一系列功能")
public class StudentController {

    @Resource
    private StudentService studentService;

    @Resource
    private TokenService tokenService;

    private Student getOneByNumberAndUserId(String number, Long id) {
        return studentService.getOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getNumber, number)
                .eq(Student::getUserId, id)
                .eq(Student::getDeleted, false));
    }

    @PostMapping
    @Operation(summary = "添加学生API", description = "使用POST请求，成功返回添加学生的信息，成功代码2030")
    public Response addStudent(@RequestBody ItemDTO dto, HttpServletRequest request) {

        if (Objects.isNull(dto) || EntityUtils.fieldIsNull(dto)) {
            throw new StudentException(ErrorCode.ILLEGAL_REQUEST_BODY, dto);
        }

        Long id = tokenService.sessionGetObject(request, "id", Long.class);

        if (Objects.nonNull(getOneByNumberAndUserId(dto.getNumber(), id))) {
            throw new StudentException(ErrorCode.STUDENT_EXIST, dto);
        }

        Student student = new Student()
                .setCreateTime(LocalDateTime.now())
                .setUserId(id);
        EntityUtils.assign(student, dto);

        studentService.save(student);

        student = getOneByNumberAndUserId(dto.getNumber(), id);

        log.info("保存学生信息成功 ====> " + student);
        return Response.success(SuccessCode.ADD_STUDENT_SUCCESS, student);
    }

    @GetMapping
    @Operation(summary = "获取学生API", description = "使用GET请求，成功返回学生的信息，成功代码2035")
    public Response getStudent(HttpServletRequest request) {

        Long id = tokenService.sessionGetObject(request, "id", Long.class);
        List<Student> students = studentService.list(new LambdaQueryWrapper<Student>()
                .eq(Student::getUserId, id)
                .eq(Student::getDeleted, false));

        List<StudentDTO> dtoList = students.stream().map(student -> {
            StudentDTO dto = StudentDTO.builder().build();
            EntityUtils.assign(dto, student);
            return dto;
        }).toList();

        return Response.success(SuccessCode.GET_STUDENT_SUCCESS, dtoList);
    }
}
