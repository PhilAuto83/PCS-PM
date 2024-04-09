package com.phildev.pcs.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class PoseidonSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/login","/error", "/").permitAll();
                    auth.requestMatchers("/secure/article-details", "/user/**","/admin/home", "/bidList/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                        })
                .formLogin(form -> form.loginPage("/login")
                        .defaultSuccessUrl("/user/list"))
                .sessionManagement(session -> session.maximumSessions(1)
                            .maxSessionsPreventsLogin(true))
                .build();
    }

    /***
     * This bean is used to publish event about session expired or session created
     * It will be used to prevent user to be logged twice due to security filter chain config
     * @return an object @see <a href="https://docs.spring.io/spring-security/site/docs/4.0.x/apidocs/org/springframework/security/web/session/HttpSessionEventPublisher.html">HttpSessionEventPublisher</a>
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /***
     * This method generates custom authentication manager bean to retrieve user from database with custom user details service and
     * bcrypt password encoder to validate password
     * @param http which is http security object used in filter chain
     * @param bCryptPasswordEncoder used by Authentication Manager to validate password
     * @return an AuthenticationManager object which will be used by authentication filter
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    WebSecurityCustomizer enableStaticResources(){
        return (web -> web.ignoring().requestMatchers("/css/**","/images/**", "/js/**"));
    }


}
