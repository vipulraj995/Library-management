package com.vraj.library.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vraj.library.filter.JwtFilter;
//import com.vraj.library.filter.JwtFilter;
import com.vraj.library.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	private static final String LOGIN_URL = "/v1/login";
//	private static final String SIGN_UP = "/v1/signup";
//	private static final String FAILURE_URL = "/login?error=true";
//	private static final String COOKIES = "JSESSIONID";
	@Autowired
	private CustomUserDetailsService userDetailsService;

	@Autowired
	private JwtFilter jwtFilter;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.headers().frameOptions().disable();
		httpSecurity.cors().disable();
		httpSecurity.csrf().disable();
		httpSecurity.authorizeRequests() 
				.antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
				//.antMatchers(HttpMethod.OPTIONS,"/v1/admin/main").permitAll()
				.antMatchers("/v1/user/info").hasAnyRole("ADMIN","USER")
				.antMatchers("/v1/admin/main").hasRole("ADMIN")
				.antMatchers("/v1/update").hasAnyRole("ADMIN","USER")
				.antMatchers("/v1/profile").hasAnyRole("ADMIN","USER")
				.antMatchers("/v1/change-password").hasAnyRole("ADMIN", "USER")
				.antMatchers("/v1/allprofile").hasRole("ADMIN")
				.antMatchers("/v1/delete").hasRole("ADMIN")
				.antMatchers("/v1/pending-approvals").hasRole("ADMIN")
				.antMatchers("/v1/approve").hasRole("ADMIN")
				.antMatchers("/v1/signup").permitAll()
				.antMatchers("/v1/authenticate").permitAll()
				.antMatchers("/h2-console/**").permitAll()
				.antMatchers("/v2/api-docs").permitAll()
				.antMatchers("/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.exceptionHandling().and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//				.and()
//				.formLogin();
		httpSecurity.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
		// .and().httpBasic();
		// .and()
		// .formLogin()
		// .loginPage(LOGIN_URL).failureUrl(FAILURE_URL)
		// .permitAll()
		// .and()
		// .logout()
		// .logoutSuccessUrl(LOGIN_URL)
		// .invalidateHttpSession(true)
		// .deleteCookies(COOKIES)
		// .permitAll();
	}


	@Bean
	public AuthenticationManager customAuthManager() throws Exception {
		return super.authenticationManagerBean();
	}

	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("ADMIN");
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
}
