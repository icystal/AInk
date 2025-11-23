package fun.icystal.aink.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    OK("0", "成功"),

    LOGIN_FAIL("1-1-0", "登录失败"),
    VERIFICATION_CODE_UNMATCH("1-1-1", "验证码错误"),

    UNKNOWN("-1", "未知异常");

    private final String code;

    private final String desc;
}
