package fun.icystal.aink.obj.vo;

import fun.icystal.aink.obj.entity.Book;
import lombok.Data;

@Data
public class Spine {

    private Long id;

    private String title;

    public Spine(Book book) {
        id = book.getId();
        title = book.getTitle();
    }

}
