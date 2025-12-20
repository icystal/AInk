package fun.icystal.aink.service.outline;

import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.context.OutlineContext;
import fun.icystal.aink.exception.AInkException;
import fun.icystal.aink.fundamental.CustomBeanOutputConverter;
import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.entity.outline.Brief;
import fun.icystal.aink.obj.entity.outline.Outline;
import fun.icystal.aink.obj.entity.outline.Sentence;
import fun.icystal.aink.util.JsonUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BriefHandler implements OutlineStepHandler {

    @Resource
    private ChatClient stepChatClient;

    private final CustomBeanOutputConverter<Brief> converter = new CustomBeanOutputConverter<>(new ParameterizedTypeReference<Brief>(){});

    @Override
    public void generate(OutlineContext context) {

        Book book = context.getBook();
        if (book.getOutline() == null || book.getOutline().getSentence() == null) {
            throw new AInkException(ResponseCode.ILLEGAL_OUTLINE_STEP);
        }

        List<Message> messages = new ArrayList<>();
        buildMessages(messages, context);

        String objContent = stepChatClient.prompt(new Prompt(messages)).call().content();
        Brief brief = JsonUtil.parseObject(objContent, Brief.class);

        book.getOutline().setBrief(brief);
    }

    private void buildMessages(List<Message> messages, OutlineContext outlineContext) {
        String originalSystemPrompt = """
                你是一个拥有无数奇思妙想，并且擅长使用三幕式结构和故事四要素设计小说结构的作家。 你需要根据一句精简的小说简介，设计出小说的结构。
                ***小说结构包含五个部分:
                    - 故事背景: 交代背景, 介绍主角和重要角色。
                    - 开端: 概括故事第一幕，以第一个灾难性事件结束，这一事件迫使主角完全投入到故事中。
                    - 发展: 概括故事第二幕的前半段，以第二个灾难性事件结束，主角扭转了之前错误的道德前提，领悟了正确的道德前提，并按照新的思路开启之后的故事。
                    - 高潮: 概括故事第二幕的后半段，以第三个灾难性事件结束，这一事件推动主角(以及反派)走向结局。
                    - 结局: 概括故事的第四部分内容，主角面临最后的对抗，或输或赢，亦或有输有赢。
                ***提示:
                    - 灾难性事件是指突然发生、彻底打破角色生活平衡、推动人物成长，并引发强烈情感冲击的关键情节。它可以是一个现实的巨大变故，也可以是人物心理的激烈挣扎。
                    - 道德前提是指故事中隐含的核心道德立场，它通过价值观冲突推动情节发展，并最终向观众传递明确的道德启示。它就像故事的灵魂，决定了人物行为的动机和整个作品的精神内核。
                ***要求:
                    - 每一部分用一句话描述即可，每部分均不超过70字。
                    - 故事符合三幕式结构，其中包含三次灾难性事件，并有一个清晰的道德前提。
                    - 重点关注灾难性事件的描写, 以及之后主角所做的决定。
                    - 无须考虑如何解决主角面临的难题, 只关注故事发展的大方向即可。
                    - 无须为角色命名，无须描写场景细节。
                """;
        messages.add(new SystemMessage(originalSystemPrompt));

        Sentence sentence = Optional.of(outlineContext).map(OutlineContext::getBook).map(Book::getOutline).map(Outline::getSentence).orElse(null);
        if (sentence != null) {
            String sentencePrompt = "***小说简介:\n";
            if (StringUtils.isNotBlank(sentence.getGenre())) {
                sentencePrompt += "类型: " + sentence.getGenre() + "\n";
            }
            if (StringUtils.isNotBlank(sentence.getIdentity())) {
                sentencePrompt += "主角身份: " + sentence.getIdentity() + "\n";
            }
            if (StringUtils.isNotBlank(sentence.getContent())) {
                sentencePrompt += "内容: " + sentence.getContent() + "\n";
            }
            messages.add(new SystemMessage(sentencePrompt));
        }

        messages.add(new SystemMessage(converter.getFormat()));
    }

    @Override
    public OutlineStep getStep() {
        return OutlineStep.BRIEF;
    }
}
