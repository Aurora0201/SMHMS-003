package top.pi1grim.ea.service;

import top.pi1grim.ea.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 学生表 服务类
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-26
 */
public interface StudentService extends IService<Student> {
    Student getOneByNumberAndUserId(String number, Long id);
}
