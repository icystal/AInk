package fun.icystal.aink.service;

import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.exception.AInkException;
import fun.icystal.aink.repository.VerificationRepository;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailVerifyService {

    private static final int mailVerificationCodeTimeout = 10;

    private static final int loginAuthTokenTimeout = 60 * 24 * 3;

    @Value("${spring.mail.username}")
    private String from;

    @Resource
    private JavaMailSender mailSender;

    @Resource
    private VerificationRepository verificationRepository;


    public void sendVerifyMail(String email) {
        String verificationCode = verificationRepository.createVerificationCode(email, mailVerificationCodeTimeout);
        String mailContent = String.format("您的登录验证码是 %s , 验证码将在%s分钟后过期", verificationCode, mailVerificationCodeTimeout);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setBcc(email);
        message.setSubject("[验证码] 您正在登录AInk");
        message.setText(mailContent);
        mailSender.send(message);
    }

    public String verify(String email, String code) {
        boolean login = verificationRepository.checkVerificationCode(email, code);
        if (!login) {
            throw new AInkException(ResponseCode.VERIFICATION_CODE_UNMATCH);
        }

        return verificationRepository.createAuthToken(email, loginAuthTokenTimeout);
    }

}
