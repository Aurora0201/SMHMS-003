package top.pi1grim.ea.service;

import top.pi1grim.ea.dto.AvatarDTO;
import top.pi1grim.ea.entity.Avatar;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 头像表 服务类
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
public interface AvatarService extends IService<Avatar> {
    void insAvatar(List<AvatarDTO> avatars);
}
