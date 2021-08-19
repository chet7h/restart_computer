package hackathon.restart.computer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.ListWattingUserRepository;

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
}
