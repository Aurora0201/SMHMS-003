package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{
    TOKEN_EXPIRATION(1000, HttpStatus.OK, "TOKEN已过期，请重新登录"),
    NO_TOKEN_CARRIED(1005, HttpStatus.OK, "访问敏感资源未携带TOKEN"),
    ;
    private final int code;
    private final HttpStatus status;
    private final String message;
}
