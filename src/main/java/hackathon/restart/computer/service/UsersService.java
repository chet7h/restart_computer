package hackathon.restart.computer.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hackathon.restart.computer.dao.UserRepository;
import hackathon.restart.computer.dto.RstListWattingUserDto;
import hackathon.restart.computer.entity.Users;

@Service
public class UsersService{
	@Autowired
	UserRepository repository;
	public List<Users> listUserByRoom(int roomId){
		return repository.listUserByRoom(roomId);
	}
	public List<Users> listUserById(List<Integer> listId){
		return repository.listUserById(listId);
	}
	public List<RstListWattingUserDto> listUserWattingByRoom(int roomId){
		return repository.listUserWattingByRoom(roomId);
	}
	
}
