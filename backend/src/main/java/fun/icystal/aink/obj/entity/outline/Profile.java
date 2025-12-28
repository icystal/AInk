package fun.icystal.aink.obj.entity.outline;

import jdk.jfr.Description;
import lombok.Data;

import java.util.List;

@Data
public class Profile {

    @Description("角色")
    private String character;

    @Description("姓名")
    private String name;

    @Description("年龄")
    private Integer age;

    @Description("性别 男/女")
    private String gender;

    @Description("身份")
    private String identity;

    @Description("价值观")
    private List<String> values;

    @Description("抱负")
    private String ambition;

    @Description("目标")
    private String goal;

    @Description("经历")
    private String experience;

}
