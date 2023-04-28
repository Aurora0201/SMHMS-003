package top.pi1grim.ea.mapper;

import top.pi1grim.ea.dto.AvatarDTO;
import top.pi1grim.ea.entity.Avatar;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 头像表 Mapper 接口
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
public interface AvatarMapper extends BaseMapper<Avatar> {
    void insAvatar(AvatarDTO avatars);
}
