package hackathon.restart.computer.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hackathon.restart.computer.entity.User_Role;

public interface User_RoleRepository extends JpaRepository<User_Role, Long>{
	@Query("select u.role_id from User_Role u where u.user_id = ?1")
	List<Integer> findByUser_Id(int user_id);
}
