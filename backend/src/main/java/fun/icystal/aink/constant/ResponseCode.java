package fun.icystal.aink.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    OK("0", "成功"),

    LOGIN_FAIL("1-1-0", "登录失败"),
    VERIFICATION_CODE_UNMATCH("1-1-1", "验证码错误"),

    BOOK_NOT_EXIST("2-1-1", "书籍不存在"),
    NO_BOOK_PERMISSION("2-1-2", "没有书籍权限"),

    UNKNOWN("-1", "未知异常");

    private final String code;

    private final String desc;
}
