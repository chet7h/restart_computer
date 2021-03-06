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
import org.springframework.security.web.session.HttpSessionEventPublisher;

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
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }
     
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
    	// C??c trang kh??ng y??u c???u login
        http.authorizeRequests().antMatchers("/", "/login**", "/logout", "/robot**").permitAll();
    	
        // N???u ch??a login, n?? s??? redirect t???i trang /login.
        http.authorizeRequests().antMatchers("/control").access("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')");
        http.authorizeRequests().antMatchers("/listWatingUser/**").access("hasAnyRole('ROLE_ADMIN','ROLE_USER')");
        // Trang ch??? d??nh cho ADMIN
        http.authorizeRequests().antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')");
        
        // Ngo???i l??? AccessDeniedException s??? n??m ra.
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        
         // C???u h??nh concurrent session
         http.sessionManagement().sessionFixation().newSession()
        .maximumSessions(1).expiredUrl("/login?message=max_session").maxSessionsPreventsLogin(true);
        
    	http.authorizeRequests().and().formLogin()//
        // Submit URL c???a trang login
        .loginProcessingUrl("/checkLogin") // Submit URL
        .loginPage("/login")//
        .defaultSuccessUrl("/default")//
        .failureUrl("/login?message=error")//
        .usernameParameter("email")//
        .passwordParameter("password")
        // C???u h??nh cho Logout Page.
        .and().logout().logoutUrl("/logout").invalidateHttpSession(true).deleteCookies("JSESSIONID").logoutSuccessUrl("/login?message=logout");
    	}
}