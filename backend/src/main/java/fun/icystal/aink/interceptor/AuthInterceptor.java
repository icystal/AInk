package fun.icystal.aink.interceptor;

import fun.icystal.aink.context.ContextHolder;
import fun.icystal.aink.repository.VerificationRepository;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    @Resource
    private VerificationRepository verificationRepository;

    @Override
    public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) {
        // 预检直接 204
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
            return false;
        }

        boolean loginStatus = verificationRepository.checkAuthToken(ContextHolder.get().getEmail(), ContextHolder.get().getToken());
        if (!loginStatus) {
            log.info("[登录态拦截器] 未登录用户请求: path: {}, email: {}", request.getRequestURI(), ContextHolder.get().getEmail());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        return true;
    }
}
