package top.pi1grim.ea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 头像表
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@Getter
@Setter
@Accessors(chain = true)
public class Avatar {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 头像链接
     */
    private String avatar;
}
