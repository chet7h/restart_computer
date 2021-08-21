package hackathon.restart.computer.dao;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import hackathon.restart.computer.entity.RoomInfo;


public interface RoomInfoReponsitory extends JpaRepository<RoomInfo, Integer>  {

	Optional<RoomInfo> findByToken(String token);
	
	Optional<RoomInfo> findById(Integer id);
	
	List<RoomInfo> findAll();
	
	@Transactional
	@Modifying()
	@Query("update RoomInfo u set u.token = null, u.user_control = null, u.update_by_user = ?2, u.update_date =?3 where u.id = ?1")
	int updateTokenRoom(int id, String user, LocalDateTime updateDate);
	
	@Query("select u.phone from Users u inner join RoomInfo r on u.id=r.idAdmin where r.id = ?1")
	String getPhoneAdminBayRoomID(int id);
	
	@Transactional
	@Modifying()
	@Query("update RoomInfo u set u.token = ?1,u.user_control = ?2, u.update_by_user = ?3, u.update_date =?4 where u.id = ?5")
	int updateTokenRoom2(String token, String user_control, String user, LocalDateTime updateDate, int id);
	
	@Transactional
	@Modifying()
	@Query("update RoomInfo u set u.stopModeFlg = ?1, u.update_by_user = ?3, u.update_date =?4 where u.id = ?2")
	int updateFlgMode(String flg, int id, String user, LocalDateTime updateDate);
}
