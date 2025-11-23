package fun.icystal.aink.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisRule {

    EMAIL_VERIFY_CODE("EMAIL_VERIFY_CODE: %s", "邮箱登录验证码"),

    LOGIN_AUTH_TOKEN("LOGIN_AUTH_TOKEN: %s", "登录态校验码"),
    ;

    public String buildKey(Object... args) {
        return String.format(format, args);
    }

    private final String format;

    private final String desc;

}
