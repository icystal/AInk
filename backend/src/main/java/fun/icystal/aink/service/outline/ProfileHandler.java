package fun.icystal.aink.service.outline;

import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.context.OutlineContext;
import fun.icystal.aink.exception.AInkException;
import fun.icystal.aink.fundamental.CustomListOutputConverter;
import fun.icystal.aink.obj.entity.Book;
import fun.icystal.aink.obj.entity.outline.Brief;
import fun.icystal.aink.obj.entity.outline.Profile;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProfileHandler implements OutlineStepHandler {

    @Resource
    private ChatClient stepChatClient;

    private final CustomListOutputConverter<Profile> converter = new CustomListOutputConverter<>(Profile.class);

    @Override
    public void generate(OutlineContext context) {
        Book book = context.getBook();
        if (book.getOutline() == null || book.getOutline().getBrief() == null) {
            throw new AInkException(ResponseCode.ILLEGAL_OUTLINE_STEP);
        }

        List<Message> messages = new ArrayList<>();
        buildMessages(messages, context);

        String objContent = stepChatClient.prompt(new Prompt(messages)).call().content();
        assert objContent != null;
        List<Profile> profiles = converter.convert(objContent);
        book.getOutline().setProfiles(profiles);
    }

    private void buildMessages(List<Message> messages, OutlineContext outlineContext) {
        String originalSystemPromptTemplate = """
                你是一个擅长根据故事情节，塑造出生动立体的人物形象的杰出作家。你需要根据一段精简的小说情节设定，设计小说中3~5名主要角色的人物形象。
                ***小说的三幕式情节:
                    故事背景: %s
                    开端: %s
                    发展: %s
                    高潮: %s
                    结局: %s
                ***每个人物的必要信息包括:
                    - 角色: 人物在故事中的位置和作用，比如: 男主角、女主角、反派、导师、朋友 等。
                    - 姓名: 人物的姓名。
                    - 性别: 男/女。
                    - 身份: 人物的社会身份，比如: 学生、警察、飞行员 等。
                    - 价值观: 人物发自内心的认同「什么是正确的」「什么是重要的」，比如: 金钱、正义、自由 等。
                    - 抱负: 人物「想成为什么样的人」，比如: 富可敌国的商人、主持正义的法官 等。
                    - 目标: 角色此刻「必须做成什么事」，比如: 获得一笔投资、通过司法考试 等。
                    - 矛盾: 人物在实现目标的过程中遇到的困难，是什么阻碍了人物实现目标。
                    - 顿悟: 人物在故事中领悟了什么道理，思想和行为发生了怎样的转变。
                    - 经历: 用一段话概括人物的个人经历，应该包含「人物在故事中的目标」「目标背后隐藏的价值观」「为了实现目标的行动」「行动过程中遇到的阻碍」「思想和行为上的转变」「最终的结局」等。
                ***创作指南:
                    - 价值观、抱负和目标，是塑造立体人物的三大核心支柱‌，它们像齿轮一样相互咬合，推动角色成长和故事发展。下面是它们的含义、联系，以及在小说创作中的作用:
                        1. 价值观
                            定义：角色内心深处认为「什么是对的、重要的、值得追求的」。这是一个人行为和判断的底层逻辑。
                            例子：正义、自由、家庭、忠诚、成功、真理、牺牲等。
                            作用：决定角色在关键时刻会如何选择。比如一个重视“家庭”的角色，可能会放弃事业机会去照顾亲人；而一个崇尚“自由”的角色，可能宁愿流浪也不愿被体制束缚。
                        2. 抱负
                            定义：角色渴望成为什么样的人，或希望人生达到怎样的状态。它比具体目标更抽象、更长远。
                            例子：「我想成为一个受人尊敬的医生」「我想改变这个不公的社会」「我想证明自己不是废物」。
                            作用：赋予角色内在驱动力和成长方向。抱负往往源于价值观——比如因为相信「知识能改变命运」，所以立志成为学者。
                        3. 目标
                            定义：角色在故事中为实现抱负而采取的具体行动或要达成的明确结果。
                            例子：「我要考上医学院」「我要揭露市长的腐败」「我要在拳赛中打败冠军」。
                            作用：推动情节发展。目标是读者最容易看到的部分，也是冲突和障碍发生的地方。
                    - 人物弧光是小说、影视等叙事艺术中一个核心概念，指的是角色在故事进程中经历的内在转变或成长轨迹。它让角色从开始到结束不再是同一个人——无论是思想、信念、情感还是行为方式都发生了深刻变化。这种变化往往由冲突、挑战和选择推动，是故事打动人心的关键。
                        1. 什么是人物弧光
                            人物弧光 = 角色的内在旅程 + 心理/道德/情感上的转变
                            如果角色始终如一、毫无改变，就叫“扁平人物”；而有弧光的角色是“立体人物”，读者能感受到他的挣扎、成长或堕落。
                        2. 人物弧光的三种主要类型
                            a. 正向弧光（Positive Arc）: 角色克服缺陷，走向更好。
                                典型模式: 从恐惧到勇气，从自私到无私，从迷茫到坚定。
                                例子: 《哈利·波特》中的纳威·隆巴顿——从懦弱自卑成长为勇敢的抵抗者。
                            b. 负向弧光（Negative Arc / Tragic Arc）: 角色被黑暗吞噬，走向毁灭或堕落。
                                典型模式: 从理想主义滑向偏执，从善良沦为残忍。
                                例子: 《麦克白》中的麦克白——因野心与迷信一步步走向疯狂与死亡。
                            c. 平弧光（Flat Arc）: 角色本身信念坚定，世界或其他人都在变化，但角色本人保持不变的信念或行为。
                                典型模式: 英雄持守真理，影响并改变周围环境。
                                例子: 《阿甘正传》中的阿甘——他几乎没变，但他的纯真影响了无数人。
                            tip: 大多数主角采用正向弧光，反派常走负向弧光，而某些象征性人物（如导师、先知）适合平弧光。
                        3. 如何塑造有效的人物弧光
                            step 1. 确立角色的“初始状态”（缺陷/错误信念）
                                ta在故事开始时相信什么？这个信念是否错误或片面？这个信念通常源于过去的创伤或误解。
                                例如：“我必须独自承担一切，不能信任别人。”
                            step 2. 设置“催化剂事件”打破旧平衡
                                故事开端的转折点迫使角色离开舒适区。
                                例如：亲人被害、被流放、发现秘密……让他不得不行动。
                            step 3. 通过冲突不断挑战其信念
                                每一次考验都逼ta面对自己的缺陷，中期ta可能反复失败、否认、逃避，甚至短暂倒退，关键时刻让ta彻底质疑旧我。
                            step 4. 在故事高潮做出“新选择”，完成转变，最终ta用新的价值观做出决定，证明他已经改变。
                                例如：曾经拒绝求助的主角，在决战中主动依靠伙伴，赢得胜利。
                            tip: 弧光的核心不是「做了什么事」，而是「为什么做这件事的理由变了」。
                    - 对于重要的角色，应该有两项或以上的价值观。因为大多数人看中的东西都不止一样，这些东西有可能无法同时获得，人的内心就会出现挣扎，而ta的行为就会变得难以预料，这将会塑造一个丰满立体的任务形象。
                    - 这里有两条思维链：在设定人物形象时，你可以从故事情节出发，思考人物应该做出什么行动 -> ta这样行动的目标 -> 人物目标之后隐藏的抱负是什么 -> ta有怎样的价值观；也可以从人物本身出发，思考人物具有怎样的价值观 -> ta的价值观是单一的吗 -> 多个价值观会塑造ta怎样的抱负 -> 多个价值观能够长久地共存吗 -> 怎样的具体矛盾会导致ta的价值观无法共存 -> ta会做出什么反应 -> 最终角色会怎样抉择。
                ***要求:
                    - 你应该返回一个数组，包含3~5名重要角色的形象。
                    - 姓名、性别、身份、目标 是人物必须的属性，其它的属性都不是必须的。比如，反派角色可能不会有什么顿悟、跑龙套的角色也可能不会有矛盾。如果你认为人物不具备这一项，可以不返回这一项，也可以返回一个空字符串。
                    - 重要角色的价值观应该至少有两项，每一项之间使用逗号分隔。
                    - 角色形象的设定需要符合故事情节，随着故事情节的发展，重要的人物心理和行为会发生变化。在角色经历中，应当注意人物弧光的塑造。
                """;

        Brief brief = outlineContext.getBook().getOutline().getBrief();
        messages.add(new SystemMessage(String.format(originalSystemPromptTemplate, brief.getBackground(), brief.getBeginning(), brief.getDevelopment(), brief.getClimax(), brief.getEnding())));
        messages.add(new SystemMessage(converter.getFormat()));
    }

    @Override
    public OutlineStep getStep() {
        return OutlineStep.PROFILE;
    }
}
