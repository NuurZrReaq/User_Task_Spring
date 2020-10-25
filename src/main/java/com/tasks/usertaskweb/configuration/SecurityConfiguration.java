package com.tasks.usertaskweb.configuration;

import services.JwtUtil;
import services.MyUserDetailsService;
import com.tasks.usertaskweb.filters.JwtFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity

public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{

    @Autowired
    MyUserDetailsService userDetailsService;
    @Autowired
    JwtFilter jwtFilter;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests().antMatchers("/authenticate").permitAll()
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtFilter,UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManager) throws Exception {
        authenticationManager.userDetailsService(userDetailsService);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder (){
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public MyUserDetailsService getUserDetailsService(){
        return new MyUserDetailsService();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtUtil jwtUtilBean() throws Exception{
        return new JwtUtil();
    }

}