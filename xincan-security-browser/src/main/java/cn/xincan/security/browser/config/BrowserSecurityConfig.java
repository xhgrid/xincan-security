package cn.xincan.security.browser.config;

import cn.xincan.security.browser.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 配置所有请求须经过身份认证
 * @author Xincan Jiang
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {


    /**
     * 注入用户详情类
     */
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;


    /**
     * 配置密码加密方式
     * @return BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 配置用授权认证信息
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }


    /**
     * 配置所有请求认证处理方式
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 默认登录（弹框）
        // http.httpBasic()
        // 表单登录
        http.formLogin()
            .and()
            .authorizeRequests()
            .anyRequest()
            .authenticated();// 任何请求都需要认证
    }
}