package Siirex.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import Siirex.service.UserAccountsService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class WebConfigSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAccountsService userDetailsService;
	
	@Bean
    PasswordEncoder passwordEncoderBean() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu Client
        return new BCryptPasswordEncoder();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoderBean());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		
		// Các trang không yêu cầu login:
		http.authorizeRequests().antMatchers("/", "/login").permitAll();
		
		// Page "/userInfo" yêu cầu phải login với vai trò ROLE_USER hoặc ROLE_ADMIN:
		// Nếu chưa login, nó sẽ được SpringSecurity tự động redirect tới trang Login default "/login"!
		http.authorizeRequests().antMatchers("/userInfo", "/createUserAccount").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");
		
		// Page "/admin" yêu cầu phải login với vai trò ROLE_ADMIN:
		// Nếu chưa login, nó sẽ được SpringSecurity tự động redirect tới trang Login default "/login"!
		http.authorizeRequests().antMatchers("/adminInfo").access("hasRole('ROLE_ADMIN')");
		
		// Thiết lập Exception khi Client truy cập vào Page không được ủy quyền:
		// Bằng cách redirect người dùng về một trang 403 nào đó!!!
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		// Cấu hình cho Login page:
		http.authorizeRequests().and().formLogin()
		.loginPage("/login")
		.loginProcessingUrl("/custom-submit")
		.failureUrl("/login?error=true")
		.usernameParameter("username")
		.passwordParameter("password");
		
		// Cấu hình cho Logout page:
		http.authorizeRequests().and().logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/logoutSuccessful");
	}
}
