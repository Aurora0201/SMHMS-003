package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CrawlerStatus implements GenericEnum{
    NOT_CREATED(10, "未创建"),
    OFFLINE(15, "离线"),
    LEAVE_UNUSED(20, "闲置"),
    DEEP_SEARCH(25, "深度搜索"),
    LISTEN(30, "监听")
    ;
    private final int code;
    private final String message;
}
