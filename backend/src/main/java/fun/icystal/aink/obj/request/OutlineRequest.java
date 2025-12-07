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
     * brief-genre  brief阶段 类别
     * brief-character  brief阶段 主角身份
     */
    private Map<String, String> extMap;

}
