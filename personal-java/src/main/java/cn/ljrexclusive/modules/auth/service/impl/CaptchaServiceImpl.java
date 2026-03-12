package cn.ljrexclusive.modules.auth.service.impl;

import cn.ljrexclusive.modules.auth.service.CaptchaService;
import org.springframework.stereotype.Service;

@Service
public class CaptchaServiceImpl implements CaptchaService {

    @Override
    public void verify(String captchaKey, String captcha) {

    }
}
