package fun.icystal.aink.service;

import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.context.ContextHolder;
import fun.icystal.aink.exception.AInkException;
import fun.icystal.aink.mapper.BookMapper;
import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.vo.Spine;
import fun.icystal.aink.util.JsonUtil;
import fun.icystal.aink.util.SnowFlake;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {

    @Resource
    private BookMapper bookMapper;

    public Book creat(String title) {
        Book book = new Book();
        long id = SnowFlake.flake();
        book.setId(id);

        book.setTitle(StringUtils.isBlank(title) ? "未命名-" + id : title);
        book.setEmail(ContextHolder.get().getEmail());

        LocalDateTime now = LocalDateTime.now();
        book.setCreateTime(now);
        book.setUpdateTime(now);

        int insert = bookMapper.insert(book);
        log.info("create {} book: {}", insert, JsonUtil.toJSONString(book));
        return book;
    }

    public void save(Book book) {
        if (book == null || book.getId() == null) {
            throw new AInkException(ResponseCode.ILLEGAL_BOOK_INFO);
        }

        book.setUpdateTime(LocalDateTime.now());
        book.setCreateTime(null);
        book.setEmail(null);
        bookMapper.updateById(book);
    }

    public void delete(Long bookId) {
        queryById(bookId);
        bookMapper.delete(bookId);
    }

    public void rename(Long bookId, String title) {
        queryById(bookId);

        Book update = new Book();
        update.setId(bookId);
        update.setTitle(title);
        update.setUpdateTime(LocalDateTime.now());
        bookMapper.updateById(update);
    }

    public Book queryById(Long bookId) {
        Book book = bookMapper.select(bookId);
        if (book == null) {
            throw new AInkException(ResponseCode.BOOK_NOT_EXIST);
        }

        if (!Objects.equals(book.getEmail(), ContextHolder.get().getEmail())) {
            throw new AInkException(ResponseCode.NO_BOOK_PERMISSION);
        }
        return book;
    }

    public List<Spine> querySpines() {
        List<Book> books = bookMapper.selectByEmail(ContextHolder.get().getEmail());
        if (CollectionUtils.isEmpty(books)) {
            return Collections.emptyList();
        }

        return books.stream().map(Spine::new).collect(Collectors.toList());
    }

}
