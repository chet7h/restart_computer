package hackathon.restart.computer.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hackathon.restart.computer.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
	@Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String email);
}
