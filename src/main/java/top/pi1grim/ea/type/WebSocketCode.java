package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WebSocketCode implements GenericEnum{

    CONNECT_DONE(3000, "连接成功"),
    UPDATE_STATUS(3005, "更新状态"),
    UPDATE_DATA(3010, "更新数据"),
    UPDATE_AVATAR(3015, "更新头像"),
    HEAD_COUNT(3020, "学生计数"),


    ;
    private final int code;
    private final String message;
}
