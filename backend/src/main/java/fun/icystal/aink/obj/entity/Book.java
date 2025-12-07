package fun.icystal.aink.obj.entity;

import fun.icystal.aink.obj.entity.outline.Outline;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {

    private Long id;

    private String title;

    private String email;

    private Outline outline;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
