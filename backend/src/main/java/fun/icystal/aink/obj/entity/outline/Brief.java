package fun.icystal.aink.obj.entity.outline;

import jdk.jfr.Description;
import lombok.Data;

@Data
public class Brief {

    @Description("故事背景")
    private String background;

    @Description("开端")
    private String beginning;

    @Description("发展")
    private String development;

    @Description("高潮")
    private String climax;

    @Description("结局")
    private String ending;

}
