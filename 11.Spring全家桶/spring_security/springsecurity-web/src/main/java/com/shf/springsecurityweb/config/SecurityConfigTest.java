package com.shf.springsecurityweb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
public class SecurityConfigTest extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(password());
    }

    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        // 赋值数据源
        jdbcTokenRepository.setDataSource(dataSource);
//         自动创建表,第一次执行会创建，以后要执行就要删除掉！
//        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        退出
        http.logout().logoutUrl("/logout")
                .logoutSuccessUrl("/test/hello").permitAll();

//        配置没有权限访问跳转自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth.html");

        http.formLogin()//自定义登录页面
                .loginPage("/login.html")//登录页面设置
                .loginProcessingUrl("/user/login")
                .defaultSuccessUrl("/test/index").permitAll()//登录访问路径
                .and().authorizeRequests()
                .antMatchers("/", "/test/hello", "/user/login").permitAll()//设置哪些路径可以直接访问，不需要认证
//                    当前登录用户，只有具备admin权限才可以访问这个路径
//                    .antMatchers("/test/index").hasAuthority("admins")
//                    .antMatchers("/test/index").hasAnyAuthority("admins,manager")
                    .antMatchers("/test/index").hasRole("sale")
                .anyRequest()
                .authenticated()
                .and().rememberMe().tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds(60)
                .userDetailsService(userDetailsService)
                .and().csrf().disable();//关闭CSRF
    }
}
