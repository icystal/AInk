package fun.icystal.aink.interceptor;

import fun.icystal.aink.context.ContextHolder;
import fun.icystal.aink.context.RequestContext;
import fun.icystal.aink.util.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class ContextInterceptor implements HandlerInterceptor {

    private static final String HEADER_EMAIL = "INK-EMAIL";

    private static final String HEADER_TOKEN = "INK-TOKEN";

    private static final String HEADER_CTX = "INK-CTX";

    @Override
    public boolean preHandle(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return false;
        }

        RequestContext ctx = new RequestContext();

        String email = request.getHeader(HEADER_EMAIL);
        if (StringUtils.isNotBlank(email)) {
            ctx.setEmail(email);
        }

        String ctxId = request.getHeader(HEADER_CTX);
        if (StringUtils.isNotBlank(ctxId)) {
            ctx.setCtxId(Long.parseLong(ctxId));
        }

        String token = request.getHeader(HEADER_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            ctx.setToken(token);
        }

        ContextHolder.set(ctx);
        log.info("[请求上下文拦截器] request path: {}, context: {}", request.getRequestURI(), JsonUtil.toJSONString(ctx));
        return true;
    }

    @Override
    public void afterCompletion(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler, Exception ex) {
        ContextHolder.remove();
    }
}
