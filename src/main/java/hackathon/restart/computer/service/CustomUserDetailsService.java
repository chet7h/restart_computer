package hackathon.restart.computer.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.UserRepository;
import hackathon.restart.computer.entity.Users;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	@Autowired
    private UserRepository userRepo;
	
	@Autowired
	private User_RoleService user_RoleService;
	
	@Autowired
	private RoleService roleService;
     
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepo.findByEmail(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        List<Integer> listRoleId = user_RoleService.findByUser_Id(users.getId());
        List<String> listRoleName = roleService.findRoleNameByRoleId(listRoleId);
        List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
        if(listRoleName != null) {
        	for (String role : listRoleName) {
                // ROLE_USER, ROLE_ADMIN,..
                GrantedAuthority authority = new SimpleGrantedAuthority(role);
                grantList.add(authority);
            }
        }
        return new CustomUser(users, grantList);
    }
}
