package hackathon.restart.computer.config;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import hackathon.restart.computer.service.CustomUserDetailsService;
 
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//    @Autowired
//	/* private DataSource dataSource; */
     
	/*
	 * @Bean public UserDetailsService userDetailsService() { return new
	 * CustomUserDetailsService(); }
	 */
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
     
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
     
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
         
        return authProvider;
    }
 
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//http.csrf().disable();
    	// Các trang không yêu cầu login
        http.authorizeRequests().antMatchers("/", "/login", "/logout", "/robot**").permitAll();
    	
        // Nếu chưa login, nó sẽ redirect tới trang /login.
        http.authorizeRequests().antMatchers("/control").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')");
        http.authorizeRequests().antMatchers("/listWatingUser/**").access("hasAnyRole('ROLE_ADMIN','ROLE_USER')");
        // Trang chỉ dành cho ADMIN
        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
        
        // Ngoại lệ AccessDeniedException sẽ ném ra.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        
    	http.authorizeRequests().and().formLogin()//
        // Submit URL của trang login
        .loginProcessingUrl("/checkLogin") // Submit URL
        .loginPage("/login")//
        .defaultSuccessUrl("/listWatingUser")//
        .failureUrl("/login?message=error")//
        .usernameParameter("email")//
        .passwordParameter("password")
        // Cấu hình cho Logout Page.
        .and().logout().logoutUrl("/logout").logoutSuccessUrl("/login?message=logout");
    	}
}