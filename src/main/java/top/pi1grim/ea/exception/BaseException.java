package top.pi1grim.ea.exception;

import lombok.Getter;
import lombok.ToString;
import top.pi1grim.ea.type.ErrorCode;


@Getter
@ToString
public class BaseException extends RuntimeException{
    private final ErrorCode errorCode;
    private final Object data;

    public BaseException(ErrorCode errorCode, Object data) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.data = data;
    }
}
