package com.bang.springboot.config.auth;

import com.bang.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity  //Spring Security 설정 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .csrf().disable() //h2-console 화면을 사용하기 위해 해당옵션 disable
                .headers().frameOptions().disable() //h2-console 화면을 사용하기 위해 해당옵션 disable
                .and()
                    .authorizeRequests() // URL별 권한 관리를 설정하는 옵션의 시작점, 'antMatchers'를 사용하기위해 필요
                    // ↓ 권한 관리 대상을 지정, URL HTTP 메소드별로 관리가능
                    .antMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll() // 전체열람가능
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name()) // USER 권한을 가진 사람만 가능
                    .anyRequest().authenticated() // 나머지 URL은 모두 인증된 사용자들에게만 허용 (로그인한 사용자)
                .and()
                    .logout()
                        .logoutSuccessUrl("/")
                .and()
                    .oauth2Login() // OAuth2 로그인 기능에 대한 설정 진입점
                        .userInfoEndpoint() // 로그인 성공 이후 사용자 정보를 가져올 때의 설정 담당
                            .userService(customOAuth2UserService); // 로그인 성공 후속조치를 진행할 UserService 인터페이스 구현체 등록


    }
}
