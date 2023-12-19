package com.tree.test.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        config -> config
                                .requestMatchers(HttpMethod.GET, "/api/v1/statements/{accountId}").hasAnyRole("USER", "ADMIN")
                                .requestMatchers(HttpMethod.GET, "/api/v1/statements/{accountId}**").hasRole("ADMIN"));
        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/login"));
        http.formLogin(Customizer.withDefaults());
        http.logout(logout -> logout.logoutUrl("/logout"));

//        //disable csrf
        http.csrf(csrf -> csrf.disable());
        return http.build();
    }

}
