package cn.xincan.security.browser.config;

import cn.xincan.security.browser.authentication.BrowserAuthenticationSuccessHandler;
import cn.xincan.security.browser.service.UserDetailsServiceImpl;
import cn.xincan.security.core.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


/**
 * @description: 配置所有请求须经过身份认证
 * @className: BrowserConfig
 * @date: 2019-07-23 18:50:22
 * @author: Xincan Jiang
 * @version: 1.0
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * @description: 注入用户详情实现类
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * @description: 注入统一配置类
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @description: 注入自定义实现登录成功处理器
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private BrowserAuthenticationSuccessHandler browserAuthenticationSuccessHandler;

    /**
     * @description: 配置密码加密方式
     * @method: passwordEncoder
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     * @return: org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
     * @exception:
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * @description: 配置用授权认证信息
     * @method: configure
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:55
     * @param: [auth: 鉴权管理构建信息对象]
     * @return: void
     * @exception: Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(this.userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    /**
     * @description: 配置所有请求认证处理方式
     * @method: configure
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:27:29
     * @param: [http: 超文本传输安全对象]
     * @return: void
     * @exception: Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // 表单登录
        http.formLogin()
            .loginPage("/authentication/require")
            .loginProcessingUrl("/authentication/form")
            .successHandler(this.browserAuthenticationSuccessHandler)
            .and()
            .authorizeRequests()
            .antMatchers("/authentication/require", this.securityProperties.getBrowser().getLoginPage()).permitAll()
            .anyRequest()
            .authenticated()// 任何请求都需要认证
            .and()
            .csrf()
            .disable();// TODO 剔除跨站攻击
    }

    /**
     * @description: 配置不参与权限认证的资源路径
     * @method: configure
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:28:47
     * @param: [web: 网络安全对象]
     * @return: void
     * @exception:
     */
    @Override
    public void configure(WebSecurity web) {

        web.ignoring().antMatchers("/assets/**","/favicon.ico");
    }
}
