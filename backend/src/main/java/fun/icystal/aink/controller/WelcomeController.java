package fun.icystal.aink.controller;

import fun.icystal.aink.constant.ResponseCode;
import fun.icystal.aink.obj.request.LoginRequest;
import fun.icystal.aink.obj.response.AInkResponse;
import fun.icystal.aink.obj.response.LoginResponse;
import fun.icystal.aink.service.MailVerifyService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/welcome")
public class WelcomeController {

    @Resource
    private MailVerifyService mailVerifyService;

    @GetMapping("/verify/{email}")
    public AInkResponse<Void> verify(@PathVariable String email) {
        mailVerifyService.sendVerifyMail(email);
        return AInkResponse.success();
    }

    @PostMapping("/login")
    public AInkResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        String token = mailVerifyService.verify(request.getEmail(), request.getVerificationCode());
        if (StringUtils.isBlank(token)) {
            return AInkResponse.fail(ResponseCode.LOGIN_FAIL);
        }

        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setEmail(request.getEmail());
        return AInkResponse.success(response);
    }

}
