package top.pi1grim.ea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import top.pi1grim.ea.entity.Student;
import top.pi1grim.ea.mapper.StudentMapper;
import top.pi1grim.ea.service.StudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 学生表 服务实现类
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-26
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Resource
    private StudentMapper studentMapper;

    public Student getOneByNumberAndUserId(String number, Long id) {
        return studentMapper.selectOne(new LambdaQueryWrapper<Student>()
                .eq(Student::getNumber, number)
                .eq(Student::getUserId, id)
                .eq(Student::getDeleted, false));
    }
}
