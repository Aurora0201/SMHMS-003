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
    LOGIN_OVERTIME(1035, HttpStatus.OK, "登录超时，请重新登录"),
    REGISTER_FAIL(1040, HttpStatus.OK, "注册失败，请重新注册"),
    WRONG_LOGIN_TIMING(1045, HttpStatus.OK, "错误的登录时机，只能在离线状态登陆"),
    ;
    private final int code;
    private final HttpStatus status;
    private final String message;
}
