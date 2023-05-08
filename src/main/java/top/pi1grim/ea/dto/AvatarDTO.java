package top.pi1grim.ea.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AvatarDTO {
    private Long stuId;
    private String avatar;
}
