package fun.icystal.aink.obj.request;

import lombok.Data;

import java.util.Map;

@Data
public class OutlineRequest {

    private Long bookId;

    private String code;

    private String mode;

    private String comment;

    /**
     * 传递各阶段的定制化参数
     * sentence-genre  sentence阶段 类别
     * sentence-identity  sentence阶段 主角身份
     */
    private Map<String, String> extMap;

}
