package top.pi1grim.ea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 学生表
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@Getter
@Setter
@Accessors(chain = true)
public class Student {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户
     */
    private Long userId;

    /**
     * 好友QQ号
     */
    private String qqNumber;

    /**
     * 备注名
     */
    private String notes;

    /**
     * 是否选中;0 -> 未选中  / 1-> 选中
     */
    @TableField("is_selected")
    private Boolean selected;

    /**
     * 是否删除;0 -> 未删除  / 1-> 删除
     */
    @TableField("is_deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
