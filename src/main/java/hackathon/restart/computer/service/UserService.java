package hackathon.restart.computer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.UserRepository;
import hackathon.restart.computer.entity.Users;

@Service
public class UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	public Users findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
