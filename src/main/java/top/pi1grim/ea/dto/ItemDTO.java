package top.pi1grim.ea.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDTO {
    private String number;
    private String notes;
}
