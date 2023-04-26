package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{
    TOKEN_EXPIRATION(1000, HttpStatus.OK, "TOKEN已过期，请重新登录"),
    NO_TOKEN_CARRIED(1005, HttpStatus.OK, "访问敏感资源未携带TOKEN"),
    ILLEGAL_REQUEST_BODY(1010, HttpStatus.OK, "请求体非法，存在空字段"),
    DUPLICATE_USERNAME(1015, HttpStatus.OK, "用户已存在，请选择其他用户名"),
    NOT_EXIST_USER(1020, HttpStatus.OK, "不存在的用户，请注册"),
    USERNAME_PASSWORD_MISMATCH(1025, HttpStatus.OK, "账号密码不匹配，请重新输入"),
    STUDENT_EXIST(1030, HttpStatus.OK, "学生已存在，请勿重复添加"),
    ;
    private final int code;
    private final HttpStatus status;
    private final String message;
}
