package YASC.Code.User;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
 
@Configuration
@Order(2)
public class WebSecurityConfig {  
//	@Autowired
//    private DataSource dataSource;
//	
	@Bean
    public UserDetailsService userDetailsService1() {
        return new CustomUserDetailsService();
    }
	
	@Bean
    public PasswordEncoder passwordEncoder2() {
        return NoOpPasswordEncoder.getInstance();
    }
 
    @Bean
    public DaoAuthenticationProvider authenticationProvider2() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService1());
        authProvider.setPasswordEncoder(passwordEncoder2());
 
        return authProvider;
    }
	
	@Bean
    public SecurityFilterChain filterChain2(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider2());
		http.antMatcher("/user/**")
        .authorizeRequests().anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/user/login")
			.usernameParameter("username")
//			.passwordParameter("password")
			.defaultSuccessUrl("/user/store")
			.failureUrl("/user/login?error=true")
			.permitAll()
			.and()
			  .rememberMe()
			    .key("uniqueAndSecret")
			    .userDetailsService(userDetailsService1())
		.and()
		.logout().logoutUrl("/user/logout").logoutSuccessUrl("/").permitAll();
		return http.build();
    }

}
