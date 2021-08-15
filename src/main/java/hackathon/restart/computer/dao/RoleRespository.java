package hackathon.restart.computer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hackathon.restart.computer.entity.Role;

public interface RoleRespository extends JpaRepository<Role, Long>{
	@Query("select r.role_name from Role r where r.id in ?1")
	List<String> findRoleNameByRoleId(List<Integer> id);

}
