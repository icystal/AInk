package fun.icystal.aink.context;

import lombok.Data;


@Data
public class RequestContext {

    private String email;

    private Long ctxId;

    private String token;

}
