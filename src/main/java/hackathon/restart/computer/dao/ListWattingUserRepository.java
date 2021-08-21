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
	
	@Transactional
	@Modifying
	@Query("delete from ListWattingUser b where b.user_id=?1")
	int deleteWattingUserLogin(int id);
	
	@Query("SELECT u.phone FROM Users u inner join ListWattingUser uw ON u.id = uw.user_id WHERE uw.room_id = ?1 ORDER BY uw.create_date ASC")
	List<String> getPhoneUserWailtingByRoomId(int roomId);
	
	@Transactional
	@Modifying
	@Query("delete from ListWattingUser b where b.room_id=?1 and b.flagcontrol=1")
	Integer deleteWattingUserControl(int room_id);
	
	/*
	 * @Transactional
	 * 
	 * @Modifying
	 * 
	 * @Query("insert into ListWattingUser l(l.id,l.user_id,l.room_id,l.flagcontrol,l.tokenroom,l.create_by_user,l.update_by_user,l.update_date,l.create_date) values (?1,?2,?3,?4,?5,?6,?7,?8,?9)"
	 * ) Integer insertWattingUser(int id,int user_id, boolean flagcontrol, String
	 * tokenroom, String create_by_user,String update_by_user,LocalDateTime
	 * update_date,LocalDateTime create_date);
	 */
	<S extends ListWattingUser> S save(S entity);
	
	@Transactional
	@Modifying()
	@Query("update ListWattingUser u set u.flagcontrol = 1 where u.id = ?1")
	int updateFlagControl(int id);
	
	
	
}
