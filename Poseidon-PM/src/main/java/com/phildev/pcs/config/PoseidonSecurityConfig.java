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

    /***
     * This bean is used to manage authorized and public endpoints
     * It has a session management layer with maximum 1 session per user
     * @param http is the root object from which we can build our custom security filter chain
     * @return SecurityFilterChain object used to filter request and add security layer
     * @throws Exception
     */
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
     * @return an HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    /***
     * This bean is a password encoder used in the app to encode user password
     * It is used by Authentication Manager to check password in database which are encoded and the password entered by user to check if they matches
     * @return a BCryptPasswordEncoder object
     */
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

    /***
     * This bean is used to grant public access to css files, images and js scripts
     * @return WebSecurityCustomizer object
     */
    @Bean
    WebSecurityCustomizer enableStaticResources(){
        return (web -> web.ignoring().requestMatchers("/css/**","/images/**", "/js/**"));
    }


}
