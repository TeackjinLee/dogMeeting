package com.springboot.dogmeeting.api.common.config;

import com.springboot.dogmeeting.api.common.config.jwt.TokenProvider;
import com.springboot.dogmeeting.api.common.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.springboot.dogmeeting.api.common.config.oauth.OAuth2UserCustomService;
import com.springboot.dogmeeting.api.common.config.oauth.OAuth2SuccessHandler;
import com.springboot.dogmeeting.api.user.UserService;
import com.springboot.dogmeeting.datasource.common.token.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebOAuthSecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;


    // 스프링 시큐리티 기능 비활성화
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/img/**", "/css/**", "/js/**");
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 토큰 방식으로 인증을 하기 때문에 기존에 사용하던 폼 로그인, 세션 비활성화
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable);

        http.sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 헤더를 확인할 커스텀 필터 추가
        http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        // 토큰 재발급 URL은 인증 없이 접근 가능하도록 설정. 나머지 API는 URL은 인증 필요
        http.authorizeHttpRequests(request ->
                    request.requestMatchers("/api/token").permitAll()
                            .requestMatchers("/api/**").authenticated()
                            .anyRequest().permitAll()
                );

        http.oauth2Login()
                .loginPage("/login")
                .authorizationEndpoint()
                // Authorization 요청과 관련된 상태 저장
                .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                .and()
                .successHandler(oAuth2SuccessHandler()) // 인증 성공 시 실행할 핸들러
                .userInfoEndpoint()
                .userService(oAuth2UserCustomService);

        http.logout(request -> request.logoutSuccessUrl("/login"));

        // /api로 시작하는 url인 경우 401 상태 코드를 반환하도록 예외 처리
        http.exceptionHandling(request ->
                request.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"))
        );

        return http.build();


        //        return http
//                .httpBasic(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize // 인증, 인가 설정
//                        .requestMatchers("/login", "/signup", "/user").permitAll()
//                        .anyRequest().authenticated()
//                ).formLogin(login -> login  // 폼 기반 로그인 설정
//                        .loginPage("/login")
//                        .defaultSuccessUrl("/articles")
//                ).logout(logout -> logout   // 로그아웃 설정
//                        .logoutSuccessUrl("/login")
//                        .invalidateHttpSession(true)
//                )
//                .build();
    }

    // 인증 관리자 관련 설정
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
//            throws Exception{
//        return http.getSharedObject(AuthenticationManagerBuilder.class)
//                .userDetailsService(userService) // 사용자 정보 서비스 설정
//                .passwordEncoder(bCryptPasswordEncoder)
//                .and()
//                .build();
//    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userService
        );
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter() {
        return new TokenAuthenticationFilter(tokenProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
