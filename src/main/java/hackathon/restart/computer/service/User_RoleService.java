package hackathon.restart.computer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.User_RoleRepository;

@Service
public class User_RoleService {
	@Autowired
	User_RoleRepository user_RoleRepository;
	List<Integer> findByUser_Id(int user_id){
		return user_RoleRepository.findByUser_Id(user_id);
	}

}
