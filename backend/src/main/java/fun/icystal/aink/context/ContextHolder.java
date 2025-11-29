package fun.icystal.aink.context;


import com.alibaba.ttl.TransmittableThreadLocal;

public class ContextHolder {
    private static final TransmittableThreadLocal<RequestContext> TTL = new TransmittableThreadLocal<>();

    public static void set(RequestContext context) {
        TTL.set(context);
    }

    public static RequestContext get() {
        return TTL.get();
    }

    public static void remove() {
        TTL.remove();
    }
}
