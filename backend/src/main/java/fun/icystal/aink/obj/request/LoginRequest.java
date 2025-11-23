package fun.icystal.aink.obj.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String email;

    private String verificationCode;

}
