package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessCode implements GenericEnum{
    REGISTER_SUCCESS(2000, "用户注册成功"),
    LOGIN_SUCCESS(2005, "用户登录成功"),
    RETURN_INFO_SUCCESS(2010, "返回用户信息成功"),
    UPDATE_INFO_SUCCESS(2015, "更新用户信息成功"),
    SESSION_PUT(2020, "Session存储数据成功"),

    ;
    private final int code;
    private final String message;
}
