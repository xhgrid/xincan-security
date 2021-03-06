package cn.xincan.security.browser.config;

import cn.xincan.security.core.authentication.AbstractChannelSecurityCofig;
import cn.xincan.security.core.authentication.mobile.SmsCodeAuthenticationSecurityConfig;
import cn.xincan.security.core.properties.SecurityConstants;
import cn.xincan.security.core.properties.SecurityProperties;
import cn.xincan.security.core.validate.code.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


/**
 * @description: 配置所有请求须经过身份认证
 * @className: BrowserConfig
 * @date: 2019-07-23 18:50:22
 * @author: Xincan Jiang
 * @version: 1.0
 */
@Configuration
public class BrowserSecurityConfig extends AbstractChannelSecurityCofig {

    /**
     * @description: 注入自定义安全配置对象实例化
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:32:13
     */
    @Autowired
    private SecurityProperties securityProperties;

    /**
     * @description: 注入数据源
     * @method:
     * @author: Xincan Jiang
     * @date: 2019-08-06 10:55:21
     */
    @Autowired
    private DataSource dataSource;

    /**
     * @description: 注入自定义用户信息处理类
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * @description: 注入自定义验证处理器
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    /**
     * @description: 注入短信登录配置
     * @author: Xincan Jiang
     * @date: 2019-07-24 10:25:26
     */
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

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
     * @description: 装配数据源，系统启动时创建persistent_logins表
     * @method:
     * @author: Xincan Jiang
     * @date: 2019-08-06 10:55:46
     */
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(this.dataSource);// 设置数据库
//        tokenRepository.setCreateTableOnStartup(true); // 系统启动时创建persistent_logins表(只执行一次，第二次启动报错说表已经存在，则将此代码注释掉)
        return tokenRepository;
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
        auth.userDetailsService(this.userDetailsService).passwordEncoder(passwordEncoder());
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
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/assets/**","/favicon.ico");
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

        // 表单登录处理
        applyPasswordAuthenticationConfig(http);

        http
                .apply(this.validateCodeSecurityConfig)
            .and()
                .apply(this.smsCodeAuthenticationSecurityConfig)
            .and()
                .rememberMe()
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(this.securityProperties.getBrowser().getRememberMeSeconds())
                .userDetailsService(this.userDetailsService)
            .and()
                .authorizeRequests()
                .antMatchers(
//                    "/error",
                    SecurityConstants.DEFAULT_UNAUTHENTICATION_URL,
                    SecurityConstants.DEFAULT_SIGN_IN_PROCESSING_URL_MOBILE,
                    this.securityProperties.getBrowser().getLoginPage(),
                    SecurityConstants.DEFAULT_VALIDATE_CODE_URL_PREFIX + "/*") // 不经过安全认证的路径
                .permitAll()
                .anyRequest()
                .authenticated()// 任何请求都需要认证
            .and()
                .csrf().disable(); // TODO 剔除跨站攻击
    }


}
