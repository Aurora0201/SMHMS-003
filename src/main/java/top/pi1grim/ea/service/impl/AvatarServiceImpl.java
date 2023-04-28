package top.pi1grim.ea.service.impl;

import jakarta.annotation.Resource;
import top.pi1grim.ea.dto.AvatarDTO;
import top.pi1grim.ea.entity.Avatar;
import top.pi1grim.ea.mapper.AvatarMapper;
import top.pi1grim.ea.service.AvatarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 头像表 服务实现类
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@Service
public class AvatarServiceImpl extends ServiceImpl<AvatarMapper, Avatar> implements AvatarService {

    @Resource
    private AvatarMapper avatarMapper;

    public void insAvatar(List<AvatarDTO> avatars) {
        avatars.forEach(avatarMapper::insAvatar);
    }
}
