package fun.icystal.aink.controller;

import fun.icystal.aink.constant.OutlineMode;
import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.context.OutlineContext;
import fun.icystal.aink.exception.AInkException;
import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.request.OutlineRequest;
import fun.icystal.aink.obj.response.AInkResponse;
import fun.icystal.aink.service.BookService;
import fun.icystal.aink.service.outline.OutlineStepHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/outline")
@Slf4j
public class OutlineController {

    private final BookService bookService;

    private final Map<OutlineStep, OutlineStepHandler> handlers;

    public OutlineController(BookService bookService, List<OutlineStepHandler> stepHandlers) {
        this.bookService = bookService;
        handlers = stepHandlers.stream().collect(Collectors.toMap(OutlineStepHandler::getStep, Function.identity()));
    }

    @PostMapping("/step")
    private AInkResponse<Book> step(@RequestBody OutlineRequest request) {
        OutlineContext context = initContext(request);

        OutlineStepHandler handler = handlers.get(context.getStep());
        switch (context.getMode()) {
            case GENERATE -> handler.generate(context);
            case REVISE -> handler.revise(context);
            case CRITICIZE -> handler.criticize(context);
        }

        return AInkResponse.success(context.getBook());
    }

    private OutlineContext initContext(OutlineRequest request) {
        OutlineContext context = new OutlineContext();

        // 大纲阶段
        OutlineStep step = OutlineStep.hit(request.getCode());
        if (step == null) {
            throw new AInkException(ResponseCode.OUTLINE_STEP_NOT_EXIST);
        }
        context.setStep(step);

        // 操作模式
        OutlineMode mode = OutlineMode.hit(request.getMode());
        if (mode == null) {
            throw new AInkException(ResponseCode.OUTLINE_MODE_NOT_EXIST);
        }

        // 目标书籍
        Book book = bookService.queryById(request.getBookId());
        if (book == null) {
            throw new AInkException(ResponseCode.BOOK_NOT_EXIST);
        }
        context.setBook(book);

        // 个性化参数
        if (CollectionUtils.isEmpty(request.getExtMap())) {
            context.setCustomRequestParam(new HashMap<>());
        } else {
            context.setCustomRequestParam(request.getExtMap());
        }

        return context;
    }

}
