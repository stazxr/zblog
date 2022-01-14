//package com.github.stazxr.zblog.base.config;
//
//import com.github.stazxr.zblog.base.security.UserDetailsServiceImpl;
//import lombok.AllArgsConstructor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
///**
// * 安全配置
// *
// * @author SunTao
// * @since 2020-11-14
// */
//@Configuration
//@AllArgsConstructor
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//    /**
//     * 安全认证用户信息来源, {@link UserDetailsServiceImpl#loadUserByUsername}
//     */
//    private final UserDetailsServiceImpl userDetailsService;
//
////    @Override
////    protected void configure(HttpSecurity http) throws Exception {
////        // 不创建和使用会话
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
////
////        // 自定义表单登录
//////        http.formLogin().loginPage(RouteEnum.LOGIN.value())
//////                .loginProcessingUrl(RouteEnum.LOGIN.value())
//////                .successHandler(authSuccessHandler)
//////                .failureHandler(authFailureHandler)
//////                .permitAll();
////
////        // 关闭CSRF跨域处理
////        http.csrf().disable();
////
////        // 验证码过滤器
////        // http.addFilterBefore(validateNumCodeFilter.getFilter(), UsernamePasswordAuthenticationFilter.class);
////
////        // http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
////    }
//
//    /**
//     * 使用 Security 推荐的 BCryptPasswordEncoder 进行加解密
//     *
//     * @return 加密手段 BCryptPasswordEncoder
//     */
//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /**
//     * authenticationProvider 配置DB的认证方式
//     *
//     * @return DaoAuthenticationProvider
//     */
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//        provider.setUserDetailsService(userDetailsService);
//        provider.setPasswordEncoder(passwordEncoder());
//        provider.setHideUserNotFoundExceptions(false);
//        return provider;
//    }
//}
