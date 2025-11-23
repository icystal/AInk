package fun.icystal.aink.exception;

import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.obj.response.AInkResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AInkException.class)
    public AInkResponse<Void> handleBizException(AInkException exception) {
        return AInkResponse.fail(exception.getResponseCode());
    }

    @ExceptionHandler(Exception.class)
    public AInkResponse<Void> handleException(Exception exception) {
        log.error("[全局异常处理] 出现未知异常", exception);
        return AInkResponse.fail(ResponseCode.UNKNOWN);
    }
}
