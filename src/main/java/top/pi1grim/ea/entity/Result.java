package top.pi1grim.ea.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 结果表
 * </p>
 *
 * @author Bin JunKai
 * @since 2023-04-28
 */
@Getter
@Setter
@Accessors(chain = true)
public class Result {

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 学生id
     */
    private Long stuId;

    /**
     * 学生QQ号
     */
    private String qqNumber;

    /**
     * 备注名
     */
    private String notes;

    /**
     * 动态内容
     */
    private String content;

    /**
     * 动态发送时间
     */
    private LocalDateTime postTime;

    /**
     * 置信度
     */
    private BigDecimal score;

    /**
     * 心理健康状况
     */
    private String status;

    /**
     * 用以区分监听还是深度;false -> 深度 true -> 监听
     */
    private Boolean type;
}
