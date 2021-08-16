package hackathon.restart.computer.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.RoomInfoReponsitory;
import hackathon.restart.computer.entity.RoomInfo;

@Service
public class RoomInfoService {

	@Autowired
	private RoomInfoReponsitory roomInfoReponsitory;
	
	public Optional<RoomInfo> findByToken(String token) {
		return roomInfoReponsitory.findByToken(token);
	}
	
	public Optional<RoomInfo> findById(Integer id) {
		return roomInfoReponsitory.findById(id);
	}
	
	public int updateTokenRoom(int roomId, String userUpdate, LocalDateTime updateTime) {
		return roomInfoReponsitory.updateTokenRoom(roomId, userUpdate, updateTime);
	}
}
