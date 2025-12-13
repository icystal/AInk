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
    ILLEGAL_BOOK_INFO("2-1-3", "非法书籍信息"),

    OUTLINE_STEP_NOT_EXIST("3-1-1", "大纲阶段不存在"),
    OUTLINE_MODE_NOT_EXIST("3-1-2", "大纲模式不存在"),

    NOT_IMPLEMENTED("-2", "尚未实现的功能"),
    UNKNOWN("-1", "未知异常");

    private final String code;

    private final String desc;
}
