package com.upiiz.SecurityInMemory.controllers.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    //SEcurity FILTER CHAIN
    //BEAN-SIngeton
    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
         return httpSecurity
                 .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http->{
                    http.requestMatchers(HttpMethod.GET, "/api/v2/facturas").hasAnyAuthority("READ");
                    http.requestMatchers(HttpMethod.POST, "/api/v2/facturas").hasAnyAuthority("CREATE");
                    http.requestMatchers(HttpMethod.PUT, "/api/v2/facturas").hasAnyAuthority("UPDATE");
                    http.requestMatchers(HttpMethod.DELETE, "/api/v2/facturas").hasAnyAuthority("DELETE");
                    //http.anyRequest().authenticated();
                    http.anyRequest().denyAll();
                })
                .build();
    }

    //MAneager
    @Bean
    public AuthenticationManager authenticationManager() throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    //Authenticanti provider
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        return daoAuthenticationProvider;
    }

    //Password encoder
    @Bean
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    //User data servuice
    @Bean
    public UserDetailsService userDetailsService(){
        //Definir usuario en memoria
        //No vamos a obtenerlos de una basade datos
        UserDetails admin = User.withUsername("admin")
                .password("admin1234")
                .roles("ADMIN")
                .authorities("READ","CREATE","UPDATE","DELETE")
                .build();

        UserDetails usuarioDali = User.withUsername("Dalila")
                .password("miguel1234")
                .roles("USER")
                .authorities("READ","UPDATE")
                .build();

        UserDetails usuarioInvitado = User.withUsername("Invitado")
                .password("miguel1234")
                .roles("USER")
                .authorities("READ")
                .build();

        List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
        userDetailsList.add(admin);
        userDetailsList.add(usuarioDali);
        userDetailsList.add(usuarioInvitado);

        return new InMemoryUserDetailsManager(userDetailsList);
    }

}
