package com.learn.springsecurity.core.verify;

import com.learn.springsecurity.core.properties.SecurityProperties;
import com.learn.springsecurity.core.verify.processor.IVerifyCodeProcessor;
import com.learn.springsecurity.core.verify.smssender.ISmsCoderSender;
import com.learn.springsecurity.core.verify.update.IVerifyCodeGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:校验码的请求
 */
@RestController
@Slf4j
public class VerifyCodeController {

    public static final String SESSION_VERIFY_IMG_CODE = "SESSION_KEY_IMG_CODE";
    public static final String SESSION_VERIFY_SMS_CODE = "SESSION_KEY_SMS_CODE";

    //用于往session中存入验证码
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Autowired
    private SecurityProperties securityProperties;
    @Autowired
    private IVerifyCodeGenerator imageVerifyCodeGenerator;
    @Autowired
    private ISmsCoderSender smsCoderSender;
    @Autowired
    private IVerifyCodeGenerator smsVerifyCodeGenerator;

    @Autowired
    private Map<String,IVerifyCodeProcessor> verifyCodeProcessors;
    /**
     * 图形验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/verifycode/image")
    public void createImgCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        ImageVerifyCode imageVerifyCode = (ImageVerifyCode) imageVerifyCodeGenerator.generateVerifyCode(servletWebRequest);
        BaseVerifyCode baseImgVerifyCode = new BaseVerifyCode(imageVerifyCode.getVerifyCode(),imageVerifyCode.getExpireTime());
        sessionStrategy.setAttribute(servletWebRequest, SESSION_VERIFY_IMG_CODE, baseImgVerifyCode);
        ImageIO.write(imageVerifyCode.getImage(), "JPEG", response.getOutputStream());
    }

    /**
     * 短信验证码
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @GetMapping("/verifycode/sms")
    public void createSmsCode(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletRequestBindingException {
        ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        BaseVerifyCode smsVerifyCode = smsVerifyCodeGenerator.generateVerifyCode(servletWebRequest);
        String mobile = ServletRequestUtils.getRequiredStringParameter(request,"mobile");
        sessionStrategy.setAttribute(servletWebRequest, SESSION_VERIFY_SMS_CODE+mobile, smsVerifyCode);
        smsCoderSender.sendSms(mobile,smsVerifyCode.getVerifyCode());
    }

//    @GetMapping("/verifycode/{type}")
//    public void createVerifyCode(HttpServletRequest request, HttpServletResponse response, @PathVariable String type) throws Exception {
//        verifyCodeProcessors.get(type+"VerifyCodeProcessor").createVerifyCode(new ServletWebRequest(request,response));
//    }
}
