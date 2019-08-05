package cn.xincan.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @description: 图片验证码异常处理类
 * @className: ValidateCodeException
 * @date: 2019/8/3 21:28
 * @author: Xincan Jiang
 * @version: 1.0
 */
public class ValidateCodeException extends AuthenticationException {

    private static final long serialVersionUID = 227981181123936804L;

    public ValidateCodeException(String msg) {
        super(msg);
    }

}
