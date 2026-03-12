package cn.ljrexclusive.modules.auth.service;

public interface CaptchaService {
    void verify(String captchaKey, String captcha);
}
