package com.project.matchingsystem.config;

import com.project.matchingsystem.enums.UserRoleEnum;
import com.project.matchingsystem.exception.CustomAccessDeniedHandler;
import com.project.matchingsystem.exception.CustomAuthenticationEntryPoint;
import com.project.matchingsystem.jwt.JwtAuthenticationFilter;
import com.project.matchingsystem.jwt.JwtProvider;
import com.project.matchingsystem.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtProvider jwtProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    private final RedisUtil redisUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toH2Console())
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeHttpRequests()
                //.antMatchers("/chat").permitAll()
                .antMatchers("/ws/*").permitAll()
                .antMatchers("/api/sign-in").permitAll()
                .antMatchers("/api/sign-up/**").permitAll()
                .antMatchers("/api/items").permitAll()
                .antMatchers("/api/items/{itemId}").permitAll()
                .antMatchers("/api/categories/**").permitAll()
                .antMatchers("/api/sellers/**").permitAll()
                .antMatchers("/api/reissue").permitAll()
                .antMatchers("/api/admin/sign-up").permitAll()
                .antMatchers("/api/seller-apply").hasRole(UserRoleEnum.USER.toString())
                .antMatchers("/api/admin/**").hasRole(UserRoleEnum.ADMIN.toString())
                .antMatchers("/api/seller/**").hasRole(UserRoleEnum.SELLER.toString())
                .anyRequest().authenticated()
                .and().addFilterBefore(new JwtAuthenticationFilter(jwtProvider, redisUtil), UsernamePasswordAuthenticationFilter.class);
        http.formLogin().disable();
        // 인증과정 실패 시 401에러 처리
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        // 권한 없을 시 403 에러 처리
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        return http.build();
    }

}
