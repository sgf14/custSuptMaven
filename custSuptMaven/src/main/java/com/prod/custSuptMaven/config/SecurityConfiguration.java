package com.prod.custSuptMaven.config;
/* class notes: entire class added by chap 26- spring security, pg 771-2
 * this is part of substantial changes to UserPrincipal and Authentication... classes as noted in chap 26.
 * note how this is imported into RootContext config class
 */
import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;

import com.prod.custSuptMaven.site.AuthenticationService;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	@Inject AuthenticationService authenticationService;
	
	//note SessionRegistry class in previous version of this app was replaced by org.springsecurity version- import above
	//SessionRegistry within app deleted entirely along with listener.  only one left is slightly modified SessionListController
	@Bean
	protected SessionRegistry sessionRegistryImpl() {
		return new SessionRegistryImpl();
	}
	
	@Override
	protected void configure (AuthenticationManagerBuilder builder) {
		builder.authenticationProvider(this.authenticationService);
	}
	
	// see pg 771- excludes static resources and possible favicon
	@Override
	public void configure(WebSecurity security) {
		security.ignoring().antMatchers("/resource/**", "/favicon.ico");
	}
	
	/* 03/20/17- app is failing to launch at this point- it gets past block above, but cant register this Spring Security filter chain somehow.  
	 * not sure how this is going wrong.  double checked the code for types in this class and code block
	 * See log files v-19 version vs custSuptMaven version.  seems like it may be related to an error in JPA EntityManagerFactory or 
	 * not getting to login page properly
	 * still working on this.
	 */
	@Override
	protected void configure(HttpSecurity security) throws Exception {
		security
			.authorizeRequests()
				.anyRequest().authenticated()
			.and().formLogin()
				.loginPage("/login").failureUrl("/login?loginFailed")
				.defaultSuccessUrl("/ticket/list")
				.usernameParameter("username")
				.passwordParameter("password")
				.permitAll()
			.and().logout()
				.logoutUrl("/logout").logoutSuccessUrl("login?loggedOut")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.permitAll()
			.and().sessionManagement()
				.sessionFixation().changeSessionId()
				.maximumSessions(1).maxSessionsPreventsLogin(true)
				.sessionRegistry(this.sessionRegistryImpl())
			.and().and().csrf()
				.requireCsrfProtectionMatcher((r) -> {
					String m = r.getMethod();
					return !r.getServletPath().startsWith("/services/") &&
							("POST".equals(m) || "PUT".equals(m) ||
									"DELETE".equals(m) || "PATCH".equals(m));
				});
	}

}
