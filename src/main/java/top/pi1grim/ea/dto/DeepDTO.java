package top.pi1grim.ea.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeepDTO {
    private int headCount;
    private String qqNumber;
    private String notes;
}
