package top.pi1grim.ea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-25
 */
@Data
@Accessors(chain = true)
public class User {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名;登录时用户名
     */
    private String username;

    /**
     * 密码;登录时密码
     */
    private String password;

    /**
     * 邮箱;发送信息时邮箱
     */
    private String mail;

    /**
     * 手机号;发送信息时邮箱
     */
    private String phone;

    /**
     * 步长;深度搜索时参数
     */
    private Byte step;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
