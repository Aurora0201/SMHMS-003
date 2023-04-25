package top.pi1grim.ea.common.response;

import lombok.Builder;
import lombok.Data;
import top.pi1grim.ea.exception.BaseException;
import top.pi1grim.ea.type.GenericEnum;


import java.time.Instant;
@Data
@Builder
public class Response {
    private int code;
    private int status;
    private String message;
    private String path;
    private Instant timestamp;
    private Object data;

    public static Response error(BaseException e, String path) {
        return builder().code(e.getErrorCode().getCode())
                .status(e.getErrorCode().getStatus().value())
                .message(e.getErrorCode().getMessage())
                .path(path)
                .timestamp(Instant.now())
                .data(e.getData())
                .build();
    }

    public static Response success(GenericEnum type, Object arg) {
        return builder().code(type.getCode())
                .status(200)
                .message(type.getMessage())
                .path("/")
                .timestamp(Instant.now())
                .data(arg)
                .build();
    }
}
