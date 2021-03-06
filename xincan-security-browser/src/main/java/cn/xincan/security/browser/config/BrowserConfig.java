package cn.xincan.security.browser.config;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;

/**
 * @description: 浏览器端配置信息
 * @className: BrowserConfig
 * @date: 2019-07-23 18:50:22
 * @author: Xincan Jiang
 * @version: 1.0
 */
@Configuration
public class BrowserConfig  {

    /**
     * @description: 配置对象转换器，将对象转换JSON字符串
     * @method: objectMapper
     * @author: Xincan Jiang
     * @date: 2019-07-24 1024:01:56
     * @return org.codehaus.jackson.map.ObjectMapper
     */
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }

    /**
     * @description: 配置当前请求缓存中的路径处理类，用于获取之前session中的请求路径
     * @method: requestCache
     * @author: Xincan Jiang
     * @date: 2019-07-24 1024:01:56
     * @return org.springframework.security.web.savedrequest.RequestCache
     */
    @Bean
    public RequestCache requestCache(){
        return new HttpSessionRequestCache();
    }

    /**
     * @description: 配置路径跳转工具类
     * @method: redirectStrategy
     * @author: Xincan Jiang
     * @date: 2019-07-24 1024:05:52
     * @return: org.springframework.security.web.RedirectStrategy
     */
    @Bean
    public RedirectStrategy redirectStrategy(){
        return new DefaultRedirectStrategy();
    }

}
