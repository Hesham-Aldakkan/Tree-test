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
    // create a public final ADMIN_ROLE variable
    public static final String ADMIN_ROLE = "ADMIN";
    public static final String USER_ROLE = "USER";

    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails user = User.builder()
                .username("user")
                .password("{noop}user")
                .roles(USER_ROLE)
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin")
                .roles(ADMIN_ROLE)
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(
                        config -> config
                                .requestMatchers(HttpMethod.GET, "/api/v1/statements/{accountId}").hasAnyRole(USER_ROLE, ADMIN_ROLE));
        http.httpBasic(Customizer.withDefaults());

        http.sessionManagement(session -> session.maximumSessions(1).maxSessionsPreventsLogin(true).expiredUrl("/login"));
        http.formLogin(Customizer.withDefaults());
        http.logout(logout -> logout.logoutUrl("/logout"));
        return http.build();
    }

}
