package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WebSocketCode implements GenericEnum{

    CONNECT_DONE(3000, "连接成功"),
    UPDATE_STATUS(3005, "更新状态"),
    SEARCH_PROCESS(3010, "深度搜索过程"),


    ;
    private final int code;
    private final String message;
}
