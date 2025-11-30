package fun.icystal.aink.service.outline;

import fun.icystal.aink.constant.OutlineStep;
import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.context.OutlineContext;
import fun.icystal.aink.exception.AInkException;

public interface OutlineStepHandler {

    default void generate(OutlineContext context) {
        throw new AInkException(ResponseCode.NOT_IMPLEMENTED);
    }

    default void revise(OutlineContext context) {
        throw new AInkException(ResponseCode.NOT_IMPLEMENTED);
    }

    default void criticize(OutlineContext context) {
        throw new AInkException(ResponseCode.NOT_IMPLEMENTED);
    }

    OutlineStep getStep();
}
