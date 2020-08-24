package com.vraj.library;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String LOGIN_URL = "/v1/login";
	private static final String SIGN_UP = "/v1/signup";
	private static final String FAILURE_URL = "/login?error=true";
	private static final String COOKIES = "JSESSIONID";
	
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors();
		httpSecurity.authorizeRequests()
			.antMatchers("/v1/main").permitAll()
			.antMatchers(SIGN_UP).permitAll()
			.antMatchers(HttpMethod.OPTIONS, "*/**").permitAll().anyRequest().authenticated()
			.and().httpBasic()
		    .and()
		    .formLogin()
		        .loginPage(LOGIN_URL).failureUrl(FAILURE_URL)
		        .permitAll()
		        .and()
		    .logout()
		    	.logoutSuccessUrl(LOGIN_URL)
		    	.invalidateHttpSession(true)
		    	.deleteCookies(COOKIES)
		        .permitAll();
	}
	
	
	@Bean
	public AuthenticationManager customAuthManager() throws Exception {
		return authenticationManager();
	}
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
	}
}
