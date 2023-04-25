package top.pi1grim.ea.exception;


import top.pi1grim.ea.type.ErrorCode;

public class TokenException extends BaseException{
    public TokenException(ErrorCode errorCode, Object data) {
        super(errorCode, data);
    }
}
