package top.pi1grim.ea.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CrawlerStatus implements GenericEnum{
    INITIAL(10, "初始化"),
    LOGIN(15, "已登录"),
    DEEP_SEARCH(20, "深度搜索"),
    LISTEN(25, "监听")
    ;
    private final int code;
    private final String message;
}
