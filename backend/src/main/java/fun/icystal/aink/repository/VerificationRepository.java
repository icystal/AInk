package fun.icystal.aink.repository;

import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class VerificationRepository {

    private final SecureRandom random = new SecureRandom();

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    /**
     * 生成 4 位随机数字的登录验证码, 存入redis并设置过期时间
     * @param email 登录邮箱
     * @param timeout 过期时间 (分钟)
     * @return 验证码
     */
    public String createVerificationCode(String email, long timeout) {
        String key = RedisRule.EMAIL_VERIFY_CODE.buildKey(email);
        String verificationCode = generateRandomCode();
        redisTemplate.opsForValue().set(key, verificationCode, timeout, TimeUnit.MINUTES);
        return verificationCode;
    }

    public boolean checkVerificationCode(String email, String verificationCode) {
        if (StringUtils.isAnyBlank(verificationCode, email)) {
            return false;
        }
        String key = RedisRule.EMAIL_VERIFY_CODE.buildKey(email);
        boolean checkResult = verificationCode.equals(redisTemplate.opsForValue().get(key));
        if (checkResult) {
            redisTemplate.delete(key);
        }
        return checkResult;
    }

    /**
     * 生成登录态token, 存入redis并设置过期时间
     * @param email 登录邮箱
     * @param timeout 过期时间 (分钟)
     * @return 验证码
     */
    public String createAuthToken(String email, long timeout) {
        String key = RedisRule.LOGIN_AUTH_TOKEN.buildKey(email);
        String token = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(key, token, timeout, TimeUnit.MINUTES);
        return token;
    }

    private String generateRandomCode() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int digit = random.nextInt(10);
            code.append((char) ('0' + digit));
        }
        return code.toString();
    }

}
