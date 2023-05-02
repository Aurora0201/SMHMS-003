package top.pi1grim.ea.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDTO {
    private Long id;
    private String qqNumber;
    private String notes;
    private Boolean selected;
}
