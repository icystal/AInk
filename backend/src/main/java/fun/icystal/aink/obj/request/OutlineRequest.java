package fun.icystal.aink.obj.request;

import lombok.Data;

@Data
public class OutlineRequest {

    private Long bookId;

    private String code;

    private String mode;

    private String comment;

}
