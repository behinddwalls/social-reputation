package com.socialreputation.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.Http401AuthenticationEntryPoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.socialreputation.oauth2.AuthenticationFilter;
import com.socialreputation.service.OAuth2TokenService;

@Configuration
@EnableWebSecurity
public class OAuthConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private OAuth2TokenService tokenAuthenticationService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// disable caching
		http.headers().cacheControl();

		http.csrf().disable() // disable csrf for our requests.
				.authorizeRequests().anyRequest().authenticated().and()
				// And filter other requests to check the presence of JWT in
				// header
				.addFilterBefore(new AuthenticationFilter(tokenAuthenticationService),
						UsernamePasswordAuthenticationFilter.class)
				.exceptionHandling().authenticationEntryPoint(new Http401AuthenticationEntryPoint(null));
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/oauth2/**");
	}
}
