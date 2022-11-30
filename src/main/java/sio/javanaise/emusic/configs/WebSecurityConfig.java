package sio.javanaise.emusic.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import sio.javanaise.emusic.services.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, jsr250Enabled = true, prePostEnabled = true)
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserService(); // (2)
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() { // (2)
		return new BCryptPasswordEncoder();
	}

	@Bean // (2)
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/e-music", "/data/**", "/personnel", "/find", "/parent/**", "/images/**",
						"/css/**", "/index", "/new/", "/new", "/h2-console/**", "/webjars/**")
				.permitAll() // (3)
				.antMatchers("/parent/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROF", "ROLE_PARENT", "ROLE_ELEVE")
				.anyRequest().authenticated() // (4)
				.and().formLogin() // (5)
				.loginPage("/login").defaultSuccessUrl("/").failureUrl("/failure/") // (5)
				.permitAll().and().logout().logoutSuccessUrl("/exit") // (6)
				.permitAll().and().httpBasic().and().exceptionHandling().accessDeniedPage("/403");
		http.headers().frameOptions().sameOrigin(); // (8)
		http.csrf().disable();
		return http.build();
	}

}