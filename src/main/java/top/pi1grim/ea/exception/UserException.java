package top.pi1grim.ea.exception;

import top.pi1grim.ea.type.ErrorCode;

public class UserException extends BaseException{
    public UserException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
