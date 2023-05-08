package top.pi1grim.ea.exception;

import top.pi1grim.ea.type.ErrorCode;

public class CrawlerException extends BaseException{
    public CrawlerException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
