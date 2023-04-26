package top.pi1grim.ea.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDTO {
    private String password;
    private String mail;
    private String phone;
    private Byte step;
}
