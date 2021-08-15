package hackathon.restart.computer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hackathon.restart.computer.entity.Users;

public class CustomUserDetails implements UserDetails {
	private Users users;
	List<GrantedAuthority> grantList;
    
    public CustomUserDetails(Users users, List<GrantedAuthority> grantList) {
        this.users = users;
        this.grantList = grantList;
    }
 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
 
    @Override
    public String getPassword() {
        return users.getPassword();
    }
 
    @Override
    public String getUsername() {
        return users.getEmail();
    }
 
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
 
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
 
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
 
    @Override
    public boolean isEnabled() {
        return true;
    }
     

}
