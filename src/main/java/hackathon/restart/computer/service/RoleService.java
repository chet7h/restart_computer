package hackathon.restart.computer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.RoleRespository;

@Service
public class RoleService {
	@Autowired
	RoleRespository roleRespository;
	List<String> findRoleNameByRoleId(List<Integer> id){
		return roleRespository.findRoleNameByRoleId(id);
	}
}
