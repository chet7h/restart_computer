package hackathon.restart.computer.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import hackathon.restart.computer.entity.Users;

public class CustomUser extends org.springframework.security.core.userdetails.User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Users users;

	public Users getUsers() {
		return users;
	}
	public void setUsers(Users users) {
		this.users = users;
	}
	public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
	public CustomUser(Users users, List<GrantedAuthority> grantList) {
		super(users.getEmail(), users.getPassword(), grantList);
		this.users = users;
	}
}
