package fun.icystal.aink.obj.response;

import fun.icystal.aink.constant.ResponseCode;
import lombok.Data;

@Data
public class AInkResponse<T> {

    private String code;

    private String message;

    private T body;

    public static <T> AInkResponse<T> success() {
        AInkResponse<T> response = new AInkResponse<>();
        response.setCode(ResponseCode.OK.getCode());
        response.setMessage(ResponseCode.OK.getDesc());
        return response;
    }

    public static <T> AInkResponse<T> success(T body) {
        AInkResponse<T> response = success();
        response.setBody(body);
        return response;
    }

    public static <T> AInkResponse<T> fail(ResponseCode responseCode) {
        AInkResponse<T> response = new AInkResponse<>();
        response.setCode(responseCode.getCode());
        response.setMessage(responseCode.getDesc());
        return response;
    }

}
