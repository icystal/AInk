package fun.icystal.aink.controller;

import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.response.AInkResponse;
import fun.icystal.aink.obj.vo.Spine;
import fun.icystal.aink.service.BookService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    private BookService bookService;

    @PostMapping("/create/{title}")
    public AInkResponse<Book> create(@PathVariable(required = false) String title) {
        Book book = bookService.creat(title);
        return AInkResponse.success(book);
    }

    @DeleteMapping("/delete/{bookId}")
    public AInkResponse<Void> delete(@PathVariable Long bookId) {
        bookService.delete(bookId);
        return AInkResponse.success();
    }

    @PostMapping("/rename/{bookId}/{title}")
    public AInkResponse<Book> rename(@PathVariable Long bookId, @PathVariable String title) {
        bookService.rename(bookId, title);
        return AInkResponse.success(bookService.queryById(bookId));
    }

    @GetMapping("/spines")
    public AInkResponse<List<Spine>> spines() {
        List<Spine> spines = bookService.querySpines();
        return AInkResponse.success(spines);
    }

    @GetMapping("/{bookId}")
    public AInkResponse<Book> query(@PathVariable Long bookId) {
        Book book = bookService.queryById(bookId);
        return AInkResponse.success(book);
    }

    @PostMapping("/save")
    public AInkResponse<Book> save(@RequestBody Book book) {
        bookService.save(book);
        return AInkResponse.success(bookService.queryById(book.getId()));
    }

}
