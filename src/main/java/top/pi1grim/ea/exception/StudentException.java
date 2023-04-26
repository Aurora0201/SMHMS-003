package top.pi1grim.ea.exception;

import top.pi1grim.ea.type.ErrorCode;

public class StudentException extends BaseException{
    public StudentException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
