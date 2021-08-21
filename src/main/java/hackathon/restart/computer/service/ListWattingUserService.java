package hackathon.restart.computer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.ListWattingUserRepository;
import hackathon.restart.computer.entity.ListWattingUser;

@Service
public class ListWattingUserService{
	@Autowired
	ListWattingUserRepository wattingUserRepository;
	List<Integer> listWattingUserByRoom(int roomId){
		return wattingUserRepository.listWattingUserByRoom(roomId);
	}
	public int deleteWattingUser(int id) {
		return wattingUserRepository.deleteWattingUser(id);
	}
	public int deleteWattingUserLogin(int id) {
		return wattingUserRepository.deleteWattingUserLogin(id);
	}
	public Integer deleteWattingUserControl(int room_id) {
		return wattingUserRepository.deleteWattingUserControl(room_id);
	}
	public ListWattingUser insertWattingUser(ListWattingUser listWattingUser) {
		return wattingUserRepository.save(listWattingUser);
	}
	
	public int updateFlagControl(int id) {
		return wattingUserRepository.updateFlagControl(id);
	}
}
