package com.learn.springsecurity.core.verify.processor;

import com.learn.springsecurity.core.verify.BaseVerifyCode;
import com.learn.springsecurity.core.verify.update.IVerifyCodeGenerator;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;
import java.util.Map;

/**
 * autor:liman
 * createtime:2021/7/10
 * comment:IVerifyCodeProcessor的抽象实现 只处理验证码（BaseVerifyCode）的类型
 */
public abstract class AbstractIVerifyCodeProcessor<T extends BaseVerifyCode> implements IVerifyCodeProcessor {

    //操作session的工具
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    /**
     * 注入这个属性的时候，spring会收集系统中所有的 {@link IVerifyCodeGenerator} 接口的实现。
     * 这个就是依赖查找
     */
//    @Autowired
    private Map<String, IVerifyCodeGenerator> verifyCodeGenerators;

    @Override
    public void createVerifyCode(ServletWebRequest request) throws Exception {
        T verifyCode = generateVerifyCode(request);//生成验证码
        saveVerifyCode(request,verifyCode);
        sendVerifyCode(request,verifyCode);
    }
    
    /**
     * 生成验证码
     * @param request
     * @return
     */
    private T generateVerifyCode(ServletWebRequest request){
        String verifyProcessorType = getVerifyProcessorType(request);
        IVerifyCodeGenerator iVerifyCodeGenerator = verifyCodeGenerators.get(verifyProcessorType);
        return (T) iVerifyCodeGenerator.generateVerifyCode(request);
    }

    /**
     * 保存验证码
     * @param request
     */
    private void saveVerifyCode(ServletWebRequest request,BaseVerifyCode verifyCode){
//        BaseVerifyCode baseVerifyCode = new BaseVerifyCode(verifyCode.getVerifyCode(), verifyCode.getExpireTime());
        sessionStrategy.setAttribute(request,SESSOIN_VERIFY_CODE_PREFIX+getVerifyProcessorType(request).toUpperCase(),verifyCode);
    }

    /**
     * 根据请求的url的后半段获取对应校验验证码的类型
     * 图形验证码请求:/verifycode/image
     * 短信验证码请求:/verifycode/sms
     * @param request
     * @return
     */
    private String getVerifyProcessorType(ServletWebRequest request){
        return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/verifycode/");
    }

    /**
     * 由于图形验证码和短信验证码的发送不一样，因此定义为抽象方法
     * @param request
     * @param verifyCode
     */
    protected abstract void sendVerifyCode(ServletWebRequest request,T verifyCode) throws Exception;
}
