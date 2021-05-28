package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MyConfig  extends WebSecurityConfigurerAdapter{

@Bean
public UserDetailsService getUserDetailService()
{
	return new UserDetailsServiceImpl();
}
	
@Bean
public BCryptPasswordEncoder passwordEncoder()
{
	return new BCryptPasswordEncoder();
}

public DaoAuthenticationProvider authenticationProvider()
{
	DaoAuthenticationProvider daoauthenticationProvider= new DaoAuthenticationProvider();
	daoauthenticationProvider.setUserDetailsService(this.getUserDetailService());
	daoauthenticationProvider.setPasswordEncoder(passwordEncoder());
	
	return daoauthenticationProvider;
}

//configure
@Override
protected void configure(AuthenticationManagerBuilder auth) throws Exception {
	// TODO Auto-generated method stub
	auth.authenticationProvider(authenticationProvider());
}

@Override
protected void configure(HttpSecurity http) throws Exception {
	// TODO Auto-generated method stub
http.authorizeRequests().antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/user/**").hasRole("USER")
.antMatchers("/**").permitAll().and().formLogin()
.loginPage("/signin")
.loginProcessingUrl("/doLogin")
.defaultSuccessUrl("/user/index")
.and().csrf().disable();

}



	
}
