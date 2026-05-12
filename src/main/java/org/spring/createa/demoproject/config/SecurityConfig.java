package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.service.AppUserDetailService;
import org.spring.createa.demoproject.service.CustomOidcUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  AppUserDetailService appUserDetailService;
  CustomOidcUserService customOidcUserService;

  @Autowired
  public SecurityConfig(AppUserDetailService appUserDetailService,
      CustomOidcUserService customOidcUserService) {
    this.appUserDetailService = appUserDetailService;
    this.customOidcUserService = customOidcUserService;
  }

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) {

    return httpSecurity
        .authorizeHttpRequests(
            auth -> auth.requestMatchers("/register", "/ping", "/login", "/signup", "/test",
                    "/oauth2",
                    "/app.css",
                    "/images/**",
                    "/fonts/**").permitAll()
                .anyRequest()
                .authenticated())
        .formLogin(
            form -> form.loginPage("/login").loginProcessingUrl("/login")
                .defaultSuccessUrl("/", true).failureUrl("/login?error=true"))
        .oauth2Login(oauth -> oauth.loginPage("/login").defaultSuccessUrl("/", true)
            .userInfoEndpoint(user -> user.oidcUserService(customOidcUserService)))
        .build();
  }

  @Bean
  AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(
        appUserDetailService);
    authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder(5));
    return authenticationProvider;
  }

}
