package fun.icystal.aink.service.outline;

import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.context.OutlineContext;
import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.entity.outline.Outline;
import fun.icystal.aink.obj.entity.outline.Sentence;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SentenceHandler implements OutlineStepHandler{

    @Resource
    private ChatClient stepChatClient;

    @Override
    public void generate(OutlineContext context) {
        Book book = context.getBook();
        if (book.getOutline() == null) {
            book.setOutline(new Outline());
        }

        Sentence sentence = new Sentence();
        book.getOutline().setSentence(sentence);

        String genre = context.getCustomRequestParam().get("sentence-genre");
        String identity = context.getCustomRequestParam().get("sentence-identity");

        if (StringUtils.isNotBlank(genre)) {
            sentence.setGenre(genre);
        }
        if (StringUtils.isNotBlank(identity)) {
            sentence.setIdentity(identity);
        }

        List<Message> messages = sentenceGeneratorPrompt(genre, identity);
        String content = stepChatClient.prompt(new Prompt(messages)).call().content();
        sentence.setContent(content);
    }

    public static List<Message> sentenceGeneratorPrompt(String genre, String identity) {
        List<Message> messages = new ArrayList<>();

        String systemPrompt = "你是一个拥有无数奇思妙想的小说作家";
        if (StringUtils.isNotBlank(genre)) {
            systemPrompt += "，正在构思一部" + genre + "类型的新作品";
        } else {
            systemPrompt += "，正在构思一部新作品";
        }
        if(StringUtils.isAnyBlank(genre, identity)) {
            systemPrompt += "，请你按照要求用一句话概括小说情节。\n";
        } else {
            systemPrompt += "，请你根据已经确定的故事设定，用一句话概括小说情节。\n";
            if (StringUtils.isNotBlank(genre)) {
                systemPrompt += "- 故事类型: " + genre + "\n";
            }
            if (StringUtils.isNotBlank(identity)) {
                systemPrompt += "- 主角身份: " + identity + "\n";
            }
        }

        systemPrompt += """
                ***要求:
                 1. 用一句话概括故事内容。
                 2. 集中于1~2个主角，说清他们在故事中需要完成的任务。
                 3. 无须交代故事背景，也可以不点明故事的结局。
                 4. 具有画面感，能够点燃读者的好奇心。
                 5. 不超过60个字。
                """;
        messages.add(new SystemMessage(systemPrompt));
        return messages;
    }

    @Override
    public OutlineStep getStep() {
        return OutlineStep.SENTENCE;
    }
}
