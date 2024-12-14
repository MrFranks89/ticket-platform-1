package it.exam.ticket.platform.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
            .requestMatchers("/api/tickets/**").permitAll()
            
            .requestMatchers("/tickets/create", "/tickets/edit/**").hasAuthority("ADMIN")
            .requestMatchers(HttpMethod.POST, "/tickets/**").hasAuthority("ADMIN")
            .requestMatchers("/operatori", "/operatori/**").hasAuthority("ADMIN")
            .requestMatchers("/tickets", "/tickets/**").hasAnyAuthority("USER", "ADMIN") 
            .requestMatchers("/note", "/note/**").hasAnyAuthority("USER", "ADMIN")
            .requestMatchers("/**").permitAll() // 

            .and().formLogin()
                .defaultSuccessUrl("/tickets", true)
            .and().logout()
            .and().csrf().disable();

        return http.build();
    }

    @Bean
    DatabaseUserDetailsService userDetailService() {
        return new DatabaseUserDetailsService();
    }
    
    @Bean
    PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
    
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}