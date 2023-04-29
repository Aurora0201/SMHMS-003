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
    SESSION_GET(2025, "获取Session数据成功"),
    ADD_STUDENT_SUCCESS(2030, "添加学生信息成功"),
    GET_STUDENT_SUCCESS(2035, "返回学生信息成功"),
    MOD_STUDENT_SUCCESS(2040, "更新学生信息成功"),
    DEL_STUDENT_SUCCESS(2045, "删除学生信息成功"),
    GET_QUICK_SUCCESS(2050, "获取登录二维码成功"),
    GET_STATUS_SUCCESS(2055, "获取状态成功"),
    STOP_CRAWLER_SUCCESS(2060, "终止Crawler成功"),
    START_DEEP_SUCCESS(2065, "启动深度搜索成功"),
    START_LISTEN_SUCCESS(2070, "启动实时监听成功"),
    UPDATE_AVATAR_SUCCESS(2075, "更新头像启动成功"),
    RETURN_ID_SUCCESS(2080, "返回用户id成功"),
    RETURN_AVATAR_SUCCESS(2085, "返回学生头像成功"),

    ;
    private final int code;
    private final String message;
}
