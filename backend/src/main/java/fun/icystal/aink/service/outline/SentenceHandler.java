package fun.icystal.aink.service.outline;

import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.context.OutlineContext;
import org.springframework.stereotype.Service;

@Service
public class SentenceHandler implements OutlineStepHandler{


    @Override
    public void generate(OutlineContext context) {
        OutlineStepHandler.super.generate(context);
    }

    @Override
    public OutlineStep getStep() {
        return OutlineStep.SENTENCE;
    }
}
