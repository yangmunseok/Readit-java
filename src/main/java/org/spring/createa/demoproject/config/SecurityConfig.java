package org.spring.createa.demoproject.config;

import org.spring.createa.demoproject.Repository.UserRepository;
import org.spring.createa.demoproject.domain.User;
import org.spring.createa.demoproject.dto.UserPrincipal;
import org.spring.createa.demoproject.service.CustomOidcUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  CustomOidcUserService customOidcUserService;
  UserRepository userRepository;

  public SecurityConfig(CustomOidcUserService customOidcUserService,
      UserRepository userRepository) {
    this.customOidcUserService = customOidcUserService;
    this.userRepository = userRepository;
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
  UserDetailsService userDetailsService() {
    return (username -> {
      User user = userRepository.findUserByName(username);
      if (user == null) {
        throw new UsernameNotFoundException("username not found");
      }
      return UserPrincipal.builder().user(user).name(user.getName()).build();
    });
  }
}
