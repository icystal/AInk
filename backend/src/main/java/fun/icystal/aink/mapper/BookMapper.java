package fun.icystal.aink.mapper;

import fun.icystal.aink.obj.entity.Book;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface BookMapper {

    @Insert("INSERT INTO book(id, title, email, book_detail, create_time, update_time) VALUES (#{id}, #{title}, #{email}, #{bookDetail}, #{createTime}, #{updateTime})")
    int insert(Book book);

    @Delete("DELETE FROM book WHERE id = #{id}")
    void delete(@Param("id") Long bookId);

    @Select("SELECT * FROM book WHERE id = #{id}")
    Book select(@Param("id") Long bookId);

    void updateById(Book book);

    @Select("SELECT * FROM book WHERE email=#{email}")
    List<Book> selectByEmail(String email);

}
