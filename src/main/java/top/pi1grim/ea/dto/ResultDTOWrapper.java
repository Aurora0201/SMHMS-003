package top.pi1grim.ea.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ResultDTOWrapper {
    private Long userId;
    private Long stuId;
    private String qqNumber;
    private String notes;
    private String content;
    private List<Integer> postTime;
    private Boolean dataType;
    private BigDecimal score;
    private String status;
}
