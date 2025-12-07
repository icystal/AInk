package fun.icystal.aink.context;

import fun.icystal.aink.constant.OutlineMode;
import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.obj.entity.Book;
import lombok.Data;

import java.util.Map;

@Data
public class OutlineContext {

    private OutlineStep step;

    private OutlineMode mode;

    private Book book;

    private String comment;

    private Map<String, String> customRequestParam;

}
