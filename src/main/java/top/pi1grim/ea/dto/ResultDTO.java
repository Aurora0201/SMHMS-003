package top.pi1grim.ea.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ResultDTO {
    private Long userId;
    private Long stuId;
    private String qqNumber;
    private String notes;
    private String content;
    private LocalDateTime postTime;
    private Boolean dataType;
    private BigDecimal score;
    private String status;
}
