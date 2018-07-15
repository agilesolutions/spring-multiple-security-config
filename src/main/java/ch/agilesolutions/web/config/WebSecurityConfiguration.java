package ch.agilesolutions.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationProvider authenticationProvider;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider);
	}

	@Configuration
	@Order(1)
	public static class ApiWebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable().antMatcher("/api/**").authorizeRequests().anyRequest().hasAnyRole("ADMIN", "API")
					.and().httpBasic();
		}
	}

	@Configuration
	@Order(2)
	public static class FormWebSecurityConfig extends WebSecurityConfigurerAdapter {

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/lib/**");
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable() // HTTP with Disable CSRF
					.authorizeRequests() // Authorize Request Configuration
					.antMatchers("/connect/**").permitAll().antMatchers("/", "/register").permitAll()
					.antMatchers("/admin/**").hasRole("ADMIN").anyRequest().authenticated().and() // Login
																									// Form
																									// configuration
																									// for
																									// all
																									// others
					.formLogin().loginPage("/login").permitAll().and() // Logout
																		// Form
																		// configuration
					.logout().permitAll();
		}
	}
}
