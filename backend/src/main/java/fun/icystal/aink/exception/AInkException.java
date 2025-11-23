package fun.icystal.aink.exception;

import fun.icystal.aink.constant.ResponseCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AInkException extends RuntimeException {

    private ResponseCode responseCode;

    public AInkException(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

}
