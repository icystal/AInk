package fun.icystal.aink.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OutlineStep {

    SENTENCE("sentence", "一句话概括"),

    BRIEF("brief", "一段式概括"),

    PROFILE("profiles", "一页纸人物介绍"),

    SKELETON("skeleton", "一页纸大纲"),

    ORIGIN("origins", "人物大纲"),

    SYNOPSIS("synopsis", "四页纸大纲"),

    BIOGRAPHY("biographies", "人物宝典"),

    SCENE("scenes", "场景清单"),

    PLOT("plot", "规划场景")
    ;

    private final String code;

    private final String desc;


    public static OutlineStep hit(String code) {
        for (OutlineStep step : values()) {
            if (step.code.equals(code)) {
                return step;
            }
        }
        return null;
    }

}
