package top.pi1grim.ea.service.impl;

import top.pi1grim.ea.entity.User;
import top.pi1grim.ea.mapper.UserMapper;
import top.pi1grim.ea.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-25
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
