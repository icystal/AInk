package fun.icystal.aink.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutlineMode {

    GENERATE("generate", "生成模式"),

    REVISE("revise", "修订模式"),

    CRITICIZE("criticize", "评论模式")
    ;

    private final String code;

    private final String desc;

    public static OutlineMode hit(String mode) {
        for (OutlineMode outlineMode : values()) {
            if (outlineMode.code.equals(mode)) {
                return outlineMode;
            }
        }
        return null;
    }

}
