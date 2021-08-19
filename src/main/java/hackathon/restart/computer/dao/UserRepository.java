package hackathon.restart.computer.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hackathon.restart.computer.dto.RstListWattingUserDto;
import hackathon.restart.computer.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long>{
	@Query("SELECT u FROM Users u WHERE u.email = ?1")
    Users findByEmail(String email);
	
	@Query("SELECT u FROM Users u WHERE u.room_id = ?1")
    List<Users> listUserByRoom(int roomId);
	
	@Query("SELECT u FROM Users u WHERE u.id in ?1")
    List<Users> listUserById(List<Integer> listId);
	
	@Query("SELECT new hackathon.restart.computer.dto.RstListWattingUserDto(u.id,u.username,u.email,u.phone,u.room_id,l.flagcontrol,l.token_room,l.id) FROM ListWattingUser l left join Users u ON l.user_id = u.id WHERE l.room_id =?1 ORDER BY l.create_date ASC")
    List<RstListWattingUserDto> listUserWattingByRoom(int roomId);
}
