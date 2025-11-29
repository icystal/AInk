package fun.icystal.aink.obj.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Book {

    private Long id;

    private String title;

    private String email;

    private BookDetail bookDetail;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}
