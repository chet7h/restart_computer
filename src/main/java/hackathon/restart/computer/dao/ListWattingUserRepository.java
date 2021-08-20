package hackathon.restart.computer.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hackathon.restart.computer.entity.ListWattingUser;

public interface ListWattingUserRepository extends JpaRepository<ListWattingUser, Long>{
	@Query("SELECT u.user_id FROM ListWattingUser u WHERE u.room_id = ?1 ORDER BY u.create_date ASC")
    List<Integer> listWattingUserByRoom(int roomId);
	
	@Transactional
	@Modifying
	@Query("delete from ListWattingUser b where b.id=?1")
	int deleteWattingUser(int id);
	
	@Query("SELECT u.phone FROM Users u inner join ListWattingUser uw ON u.id = uw.user_id WHERE uw.room_id = ?1 ORDER BY uw.create_date ASC")
	List<String> getPhoneUserWailtingByRoomId(int roomId);
	
	@Transactional
	@Modifying
	@Query("delete from ListWattingUser b where b.room_id=?1 and b.flagcontrol=1")
	Integer deleteWattingUserControl(int room_id);
}
